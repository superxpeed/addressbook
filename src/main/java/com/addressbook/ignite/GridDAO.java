package com.addressbook.ignite;

import com.addressbook.LockRecordException;
import com.addressbook.UniversalFieldsDescriptor;
import com.addressbook.dto.*;
import com.addressbook.model.*;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteTransactions;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.*;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.transactions.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

import javax.cache.Cache;
import java.lang.reflect.Constructor;
import java.sql.Timestamp;
import java.util.*;

public class GridDAO {

    private static Logger logger = LoggerFactory.getLogger(GridDAO.class);

    private static Ignite ignite;

    static void startClient() {
        if (ignite != null) return;
        try {

            IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
            igniteConfiguration.setPeerClassLoadingEnabled(true);
            igniteConfiguration.setClientMode(true);
            igniteConfiguration.setIncludeEventTypes(org.apache.ignite.events.EventType.EVT_TASK_STARTED,
                    org.apache.ignite.events.EventType.EVT_TASK_FINISHED,
                    org.apache.ignite.events.EventType.EVT_TASK_FAILED,
                    org.apache.ignite.events.EventType.EVT_TASK_TIMEDOUT,
                    org.apache.ignite.events.EventType.EVT_TASK_SESSION_ATTR_SET,
                    org.apache.ignite.events.EventType.EVT_TASK_REDUCED,
                    org.apache.ignite.events.EventType.EVT_CACHE_OBJECT_PUT,
                    org.apache.ignite.events.EventType.EVT_CACHE_OBJECT_READ,
                    org.apache.ignite.events.EventType.EVT_CACHE_OBJECT_REMOVED);

            TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
            TcpDiscoveryMulticastIpFinder tcpDiscoveryMulticastIpFinder = new TcpDiscoveryMulticastIpFinder();
            tcpDiscoveryMulticastIpFinder.setAddresses(Collections.singleton("127.0.0.1:47500..47509"));
            tcpDiscoverySpi.setIpFinder(tcpDiscoveryMulticastIpFinder);
            igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);
            ignite = Ignition.start(igniteConfiguration);
            ignite.cluster().active(true);
            for (Map.Entry<String, Class<?>> cache : UniversalFieldsDescriptor.getCacheClasses().entrySet()) {
                CacheConfiguration<String, ?> cfg = new CacheConfiguration<>();
                cfg.setCacheMode(CacheMode.PARTITIONED);
                cfg.setName(cache.getKey());
                cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
                cfg.setStatisticsEnabled(true);
                cfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
                cfg.setIndexedTypes(String.class, cache.getValue());
                try (IgniteCache createdCache = ignite.getOrCreateCache(cfg)) {
                    if (ignite.cluster().forDataNodes(createdCache.getName()).nodes().isEmpty()) {
                        logger.info("");
                        logger.info(">>> Please start at least 1 remote cache node.");
                        logger.info("");
                    }
                }
            }
            MenuCreator.initMenu();
            UserCreator.initUsers();
        } catch (Exception e) {
            logger.error("Exception during Ignite Client startup: ", e);
        }
    }

    public static OrganizationDto createOrUpdateOrganization(OrganizationDto organizationDto, String user) {
        IgniteCache<String, Organization> cachePerson = ignite.getOrCreateCache(UniversalFieldsDescriptor.ORGANIZATION_CACHE);
        Organization organization = cachePerson.get(organizationDto.getId());
        if (organization == null) {
            organization = new Organization(organizationDto);
        } else {
            if (notLockedByUser(organization.getId(), user))
                throw new LockRecordException("Record was not locked by " + user);
        }
        organization.setType(OrganizationType.values()[Integer.valueOf(organizationDto.getType())]);
        organization.setLastUpdated(new Timestamp(System.currentTimeMillis()));
        organization.setName(organizationDto.getName());
        organization.setAddr(new Address(organizationDto.getStreet(), organizationDto.getZip()));
        cachePerson.put(organization.getId(), organization);
        return organizationDto;
    }

    public static PersonDto createOrUpdatePerson(PersonDto personDto, String user) {
        IgniteCache<String, Person> cachePerson = ignite.getOrCreateCache(UniversalFieldsDescriptor.PERSON_CACHE);
        Person person = cachePerson.get(personDto.getId());
        if (person == null) {
            person = new Person(personDto);
        } else {
            if (notLockedByUser(person.getId(), user))
                throw new LockRecordException("Record was not locked by " + user);
        }
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        person.setOrgId(personDto.getOrgId());
        person.setSalary(personDto.getSalary());
        person.setResume(personDto.getResume());
        cachePerson.put(person.getId(), person);
        return personDto;
    }

    public static List<ContactDto> createOrUpdateContacts(List<ContactDto> contactDtos, String user) {
        if (contactDtos.isEmpty() || user == null) return contactDtos;
        IgniteCache<String, Person> cachePerson = ignite.getOrCreateCache(UniversalFieldsDescriptor.PERSON_CACHE);
        Person person = cachePerson.get(contactDtos.get(0).getPersonId());
        if (person == null) {
            throw new IllegalArgumentException("Parent person with id " + contactDtos.get(0).getPersonId() + " doesn't exist");
        } else {
            if (notLockedByUser(person.getId(), user))
                throw new LockRecordException("Record was not locked by " + user);
        }
        IgniteCache<String, Contact> cacheContacts = ignite.getOrCreateCache(UniversalFieldsDescriptor.CONTACT_CACHE);
        IgniteTransactions transactions = ignite.transactions();
        try (Transaction tx = transactions.txStart()) {
            for (ContactDto contactDto : contactDtos) {
                Contact contact = cacheContacts.get(contactDto.getId());
                if (contact == null) contact = new Contact();
                contact.setData(contactDto.getData());
                contact.setDescription(contactDto.getDescription());
                contact.setPersonId(contactDto.getPersonId());
                contact.setType(ContactType.values()[Integer.valueOf(contactDto.getType())]);
                cacheContacts.put(contact.getContactId(), contact);
            }
            tx.commit();
        }
        return contactDtos;
    }

    static void createOrUpdateUser(User newUser) {
        IgniteCache<String, User> cacheUser = ignite.getOrCreateCache(UniversalFieldsDescriptor.USER_CACHE);
        User user = cacheUser.get(newUser.getLogin());
        if (user != null) {
            user.setPassword(newUser.getPassword());
            user.setRoles(newUser.getRoles());
        } else user = newUser;
        cacheUser.put(user.getLogin(), user);
    }

    private static boolean notLockedByUser(String key, String user) {
        if (user == null) return false;
        IgniteCache<String, String> cacheLocks = ignite.getOrCreateCache(UniversalFieldsDescriptor.LOCK_RECORD_CACHE);
        String userLocked = cacheLocks.get(key);
        return !user.equals(userLocked);
    }

    public static boolean lockUnlockRecord(String key, String user, boolean lock) {
        IgniteCache<String, String> cacheLocks = ignite.getOrCreateCache(UniversalFieldsDescriptor.LOCK_RECORD_CACHE);
        if (lock) return cacheLocks.putIfAbsent(key, user);
        else return cacheLocks.remove(key, user);
    }

    public static void unlockAllRecordsForUser(String user) {
        IgniteCache<String, String> cacheLocks = ignite.getOrCreateCache(UniversalFieldsDescriptor.LOCK_RECORD_CACHE);
        cacheLocks.removeAll(new HashSet<>(cacheLocks.query(new ScanQuery<String, String>((k, v) -> v.equals(user)), Cache.Entry::getKey).getAll()));
    }

    public static User getUserByLogin(String login) {
        IgniteCache<String, User> cacheUser = ignite.getOrCreateCache(UniversalFieldsDescriptor.USER_CACHE);
        return cacheUser.get(login);
    }

    static void clearMenus() { ignite.getOrCreateCache(UniversalFieldsDescriptor.MENU_CACHE).clear(); }

    static MenuEntryDto createOrUpdateMenuEntry(MenuEntryDto menuEntryDto, MenuEntryDto parentEntryDto) {
        IgniteCache<String, MenuEntry> cachePerson = ignite.getOrCreateCache(UniversalFieldsDescriptor.MENU_CACHE);
        MenuEntry menuEntry;
        if (menuEntryDto.getId() != null) menuEntry = cachePerson.get(menuEntryDto.getId());
        else menuEntry = new MenuEntry();
        menuEntry.setName(menuEntryDto.getName());
        menuEntry.setUrl(menuEntryDto.getUrl());
        menuEntry.setRoles(menuEntryDto.getRoles());
        if (parentEntryDto != null) menuEntry.setParentId(parentEntryDto.getId());
        menuEntryDto.setId(menuEntry.getId());
        cachePerson.put(menuEntry.getId(), menuEntry);
        return menuEntryDto;
    }

    private static List<Cache.Entry<String, MenuEntry>> checkIfMenuExists(IgniteCache<String, MenuEntry>  menuCache, String url){
        List<Cache.Entry<String, MenuEntry>> entries = menuCache.query(new SqlQuery<String, MenuEntry>(MenuEntry.class, "url = ?").setArgs(url)).getAll();
        if (entries.isEmpty()) throw new IllegalArgumentException("Menu with url: " + url + " doesn't exist");
        return entries;
    }

    public static List<MenuEntryDto> readNextLevel(String url, Collection<? extends GrantedAuthority> authorities) {
        IgniteCache<String, MenuEntry> cache = ignite.getOrCreateCache(UniversalFieldsDescriptor.MENU_CACHE);
        List<Cache.Entry<String, MenuEntry>> entries = checkIfMenuExists(cache, url);
        MenuEntry menuEntry = entries.get(0).getValue();
        SqlQuery<String, MenuEntry> sql = new SqlQuery<>(MenuEntry.class, "parentId = ?");
        List<MenuEntryDto> menuEntryDtos = new ArrayList<>();
        try (QueryCursor<Cache.Entry<String, MenuEntry>> cursor = cache.query(sql.setArgs(menuEntry.getId()))) {
            for (Cache.Entry<String, MenuEntry> e : cursor) {
                for (GrantedAuthority authority : authorities) {
                    if (e.getValue().getRoles() != null && e.getValue().getRoles().contains(authority.getAuthority().replace("ROLE_", ""))) {
                        menuEntryDtos.add(new MenuEntryDto(e.getValue()));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error while reading next menu level:", e);
        }
        return menuEntryDtos;
    }

    public static List<Breadcrumb> readBreadcrumbs(String url) {
        IgniteCache<String, MenuEntry> cache = ignite.getOrCreateCache(UniversalFieldsDescriptor.MENU_CACHE);
        List<Cache.Entry<String, MenuEntry>> entries = checkIfMenuExists(cache, url);
        MenuEntry original = entries.get(0).getValue();
        MenuEntry menuEntry = original;
        List<Cache.Entry<String, MenuEntry>> menuEntries;
        List<Breadcrumb> breadcrumbs = new ArrayList<>();
        if (menuEntry.getParentId() == null) return breadcrumbs;
        SqlQuery<String, MenuEntry> sql = new SqlQuery<>(MenuEntry.class, "id = ?");
        while (true) {
            menuEntries = cache.query(sql.setArgs(menuEntry.getParentId())).getAll();
            if (!menuEntries.isEmpty()) {
                menuEntry = menuEntries.get(0).getValue();
                breadcrumbs.add(0, new Breadcrumb(menuEntry.getName(), menuEntry.getUrl()));
            } else break;
        }
        breadcrumbs.add(new Breadcrumb(original.getName(), original.getUrl()));
        return breadcrumbs;
    }

    private static StringBuilder getQuerySql(List<FilterDto> filterDto) {
        StringBuilder baseSql = new StringBuilder(" ");
        if (filterDto.size() != 0) {
            for (FilterDto filter : filterDto) {
                String type = filter.getType();
                String addSql = "";
                if (type.equals("NumberFilter")) {
                    Integer query;
                    try {
                        query = Integer.valueOf(filter.getValue());
                    } catch (NumberFormatException e) {
                        logger.info("Invalid filter argument: " + filter.getValue());
                        continue;
                    }
                    addSql = filter.getName() + getComparator(filter) + query;
                }
                if (type.equals("TextFilter")) {
                    addSql = filter.getName() + " like '%" + filter.getValue().replaceAll("'", "''") + "%'";
                }
                if (type.equals("DateFilter")) {
                    addSql = filter.getName() + getComparator(filter) + "'" + filter.getValue() + "'";
                }
                if (filterDto.indexOf(filter) == 0) baseSql.append(" where ");
                baseSql.append(addSql);
                if (filterDto.indexOf(filter) != (filterDto.size() - 1)) baseSql.append(" and ");
            }
        }
        return baseSql;
    }

    public static List<?> selectCachePage(int page, int pageSize, String sortName, String sortOrder, List<FilterDto> filterDto, String cacheName) {
        IgniteCache<String, Object> cache = ignite.getOrCreateCache(cacheName);
        List<Object> cacheDtoArrayList = new ArrayList<>();
        SqlQuery<String, Object> sql = new SqlQuery<>(UniversalFieldsDescriptor.getCacheClass(cacheName), getQuerySql(filterDto)
                .append(" order by ")
                .append(sortName).append(" ")
                .append(sortOrder)
                .append(" limit ? offset ?")
                .toString());
        try (QueryCursor<Cache.Entry<String, Object>> cursor = cache.query(sql.setArgs(pageSize, (page - 1) * pageSize))) {
            Constructor dtoConstructor = UniversalFieldsDescriptor.getDtoClass(cacheName).getConstructor(UniversalFieldsDescriptor.getCacheClass(cacheName));
            for (Cache.Entry e : cursor)
                cacheDtoArrayList.add(dtoConstructor.newInstance(e.getValue()));
        } catch (Exception e) {
            logger.error("Error while selecting data for next page in table:", e);
        }
        return cacheDtoArrayList;
    }

    public static List<ContactDto> getContactsByPersonId(String id) {
        IgniteCache<String, Contact> cache = ignite.getOrCreateCache(UniversalFieldsDescriptor.CONTACT_CACHE);
        List<ContactDto> cacheDtoArrayList = new ArrayList<>();
        SqlQuery<String, Contact> sql = new SqlQuery<>(Contact.class, "personId = ?");
        try (QueryCursor<Cache.Entry<String, Contact>> cursor = cache.query(sql.setArgs(id))) {
            for (Cache.Entry<String, Contact> e : cursor)
                cacheDtoArrayList.add(new ContactDto(e.getValue()));
        } catch (Exception e) {
            logger.error("Error while retrieving contacts for person:", e);
        }
        return cacheDtoArrayList;

    }

    private static String getComparator(FilterDto filterDto) {
        if (filterDto.getComparator() == null || filterDto.getComparator().equals(""))
            return " = ";
        else return " " + filterDto.getComparator() + " ";
    }

    public static Integer getTotalDataSize(String cacheName, List<FilterDto> filterDto) {
        if (filterDto.size() == 0) {
            IgniteCache cache = ignite.getOrCreateCache(cacheName);
            return cache.size(CachePeekMode.ALL);
        }
        IgniteCache<String, Object> cache = ignite.getOrCreateCache(cacheName);
        SqlQuery<String, Object> sql = new SqlQuery<>(UniversalFieldsDescriptor.getCacheClass(cacheName), getQuerySql(filterDto).toString());
        try (QueryCursor<Cache.Entry<String, Object>> cursor = cache.query(sql)) {
            return cursor.getAll().size();
        } catch (Exception e) {
            logger.error("Error while retrieving total data size for table:", e);
        }
        return 0;
    }

    static void stopClient() {
        if (ignite != null) {
            try {
                ignite.close();
            } catch (Exception e) {
                logger.error("Error during Ignite client stopping:", e);
            }
        }
    }

    public static Map<String, CacheMetrics> getCacheMetrics() {
        Map<String, CacheMetrics> cacheMetricsMap = new HashMap<>();
        for (String cacheName : UniversalFieldsDescriptor.getCacheClasses().keySet()) {
            cacheMetricsMap.put(cacheName, ignite.getOrCreateCache(cacheName).metrics());
        }
        return cacheMetricsMap;
    }
}
