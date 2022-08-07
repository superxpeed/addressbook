## Kubernetes installation

### Install Docker Desktop
```shell
https://www.docker.com/products/docker-desktop/
```
### Install ansible using brew/apt 
```shell
brew install ansible
```
```shell
sudo apt install ansible
sudo apt install python3-pip
pip3 install kubernetes
pip3 install openshift
```
### Install Kubernetes Dashboard
```shell
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.6.0/aio/deploy/recommended.yaml
```
### Ingress NGINX server
```shell
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.3.0/deploy/static/provider/cloud/deploy.yaml
```
#### NOTE: With Ingress configured in the default mode X509 client authentication for Spring doesn't work
#### Or you can enable SSL passthrough:
```shell
https://kubernetes.github.io/ingress-nginx/user-guide/tls/#ssl-passthrough
```
### Create user:
```shell
kubectl apply -f dashboard-adminuser.yml
```
### Create role binding:
```shell
kubectl apply -f admin-role-binding.yml
```
### Create token and [add it here](https://github.com/dredwardhyde/addressbook/blob/master/devops/ansible/vars/variables.yaml#L5):
```shell
kubectl -n kubernetes-dashboard create token admin-user
```
### Start Kubernetes Dashboard proxy:
```shell
kubectl proxy
```
### Kubernetes Dashboard URL:
```shell
http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/login
```

### Install Istio:
```shell
# Download latest Istio
https://github.com/istio/istio/releases

# Unpack and add istio-1.14.3\bin to the $PATH
PATH=istio-1.14.3/bin:$PATH

# Install Istio
istioctl install -y --set profile=demo --set meshConfig.outboundTrafficPolicy.mode=REGISTRY_ONLY
```

### Deploy project:
```shell
cd $PROJECT_ROOT/devops/ansible
# Ingress, Egress, Eureka, Ignite, WebApp 
ansible-playbook delete.yaml --tags "ingress, egress, eureka, ignite, web"
ansible-playbook deploy.yaml --tags "ingress, egress, eureka, ignite, web"

# Ingress, Egress, Eureka, Mongo, WebApp
ansible-playbook delete.yaml --tags "ingress, egress, eureka, mongo, web"
ansible-playbook deploy.yaml --tags "ingress, egress, eureka, mongo, web"

# Ingress, Egress, Eureka, PostgreSQL, WebApp
ansible-playbook delete.yaml --tags "ingress, egress, eureka, postgre, web"
ansible-playbook deploy.yaml --tags "ingress, egress, eureka, postgre, web"
```  
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/ansible_wsl2.png" width="900"/>  

<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/deployments.png" width="900"/>  

<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/pods.png" width="900"/>  

<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/istio_proxy.png" width="900"/>  

<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/egress.png" width="900"/>  

### UI with Kubernetes
```shell
https://http.localdev.me/#/
```
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/client_auth.png" width="900"/> 

## Docker installation

### Install Docker Desktop
```shell
https://www.docker.com/products/docker-desktop/
```

### Create Volumes
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/volumes.png" width="900"/>  

### Copy [keystore.jks](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/certs/keystore.jks), [truststore.jks](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/certs/truststore.jks), [logback.xml](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/resources/logback.xml) to the Volume folders
```shell
\\wsl$\docker-desktop-data\version-pack-data\community\docker\volumes
```
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/logback_volume.png" width="900"/>
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/certs_volume.png" width="900"/>  

### Deploy project
```shell
docker-compose -f addressbook/devops/docker-compose.yml up -d
```
or 
```shell
docker-compose -f addressbook/devops/docker-compose-ignite.yml up -d
```
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/docker.png" width="900"/>

### UI with Docker
```shell
https://localhost:10000/#/
```
