## Kubernetes installation

### Install Docker Desktop and enable Kubernetes
```shell
https://www.docker.com/products/docker-desktop/
```
### Enable file permissions for WSL and restart Docker/Kubernetes
```shell
sudo vim /etc/wsl.conf
...
[automount]
options = "metadata"
enabled = true
...
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

### Get Docker IP address
```shell
docker run -it --rm alpine nslookup host.docker.internal
```
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/nslookup.png" width="500"/>  

### Add aliases for <ins>host.docker.internal</ins> and restart WSL/Docker/Kubernetes
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/hosts.png" width="500"/>  

### Ingress NGINX server
```shell
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.3.0/deploy/static/provider/cloud/deploy.yaml
```
#### Enable SSL passthrough
```shell
https://kubernetes.github.io/ingress-nginx/user-guide/tls/#ssl-passthrough
```
### Create user
```shell
kubectl apply -f dashboard-adminuser.yml
```
### Create role binding
```shell
kubectl apply -f admin-role-binding.yml
```
### Create token and [add it here](https://github.com/dredwardhyde/addressbook/blob/master/devops/ansible/vars/user-variables.yaml#L6)
```shell
kubectl -n kubernetes-dashboard create token admin-user
```
### Start Kubernetes Dashboard proxy
```shell
kubectl proxy
```
Or to access Kubernetes dashboard via IP address:
```shell
kubectl proxy --address="0.0.0.0" --port=8001 --accept-hosts="^.*$"
```
### Kubernetes Dashboard URL
```shell
http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/login
```

### Install Istio, Prometheus, and Kiali
```shell
# Download latest Istio
https://github.com/istio/istio/releases

# Unpack and add istio-1.14.3\bin to the $PATH
PATH=istio-1.14.3/bin:$PATH

# Install Istio
istioctl install -y --set profile=demo --set meshConfig.outboundTrafficPolicy.mode=REGISTRY_ONLY

# Turn on Envoy sidecar injection for default namespace
kubectl label namespace default istio-injection=enabled

# Install Prometheus
kubectl apply -f ${ISTIO_HOME}/samples/addons/prometheus.yaml

# Install Kiali
kubectl apply -f ${ISTIO_HOME}/samples/addons/kiali.yaml
```
### Enable access to the Kiali UI
```shell
kubectl port-forward svc/kiali 20001:20001 -n istio-system
```
### Kiali UI
```shell
http://localhost:20001/
```

<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/istio_installation.png" width="700"/>  

### Deploy project
```shell
cd $PROJECT_ROOT/devops/ansible
sudo chmod o-w .
chmod -x password_file

# Ingress, Egress, Eureka, Ignite, WebApp, Fluent Bit 
ansible-playbook delete.yaml --tags "ingress, egress, eureka, ignite, web, istio_https, fluent_bit_web"
ansible-playbook deploy.yaml --tags "ingress, egress, eureka, ignite, web, istio_https, fluent_bit_web"
```
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/ignite_kiali.png" width="900"/>  

```shell
# Ingress, Egress, Eureka, Mongo, WebApp, Fluent Bit
ansible-playbook delete.yaml --tags "ingress, egress, eureka, mongo, web, istio_https, fluent_bit_web"
ansible-playbook deploy.yaml --tags "ingress, egress, eureka, mongo, web, istio_https, fluent_bit_web"
```
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/mongo_kiali.png" width="900"/>  

```shell
# Ingress, Egress, Eureka, PostgreSQL, WebApp, Fluent Bit
ansible-playbook delete.yaml --tags "ingress, egress, eureka, postgre, web, istio_https, fluent_bit_web"
ansible-playbook deploy.yaml --tags "ingress, egress, eureka, postgre, web, istio_https, fluent_bit_web"
```
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/postgre_kiali.png" width="900"/>  


### Ansible project deployment  
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/ansible_wsl2.png" width="900"/>  

### Deployments  
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/deployments.png" width="900"/>  

### Pods  
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/pods.png" width="900"/>  

### Envoy sidecar logs  
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/istio_proxy.png" width="900"/>  

### Egress logs
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/egress.png" width="900"/>  

### X.509 Authentication with <ins>web_ssl_enabled</ins> set to <ins>true</ins> and <ins>istio_x509</ins> role deployed
```shell
https://simple-https.localdev.me/#/
```
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/client_auth.png" width="900"/> 

### TLS with <ins>web_ssl_enabled</ins> set to <ins>false</ins> and <ins>istio_https</ins> role deployed
```shell
https://istio-https.localdev.me/#/
```
### Ingress logs
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/ingress.png" width="900"/>  

### Web application envoy sidecar logs
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/istio_proxy_web.png" width="900"/> 

### Fluent Bit sidecar logs with <ins>fluent_bit_enabled</ins> set to <ins>true</ins> and <ins>fluent_bit_web</ins> role deployed
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/fluent_bit.png" width="900"/> 

### Certificate
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/istio_https.png" width="900"/> 

### Web application logs in Elastic with <ins>fluent_bit_enabled</ins> set to <ins>true</ins> and <ins>fluent_bit_web</ins> role deployed
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/fluent_bit_elastic.png" width="900"/> 
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/fluent_bit_elastic_expanded.png" width="900"/> 

### Metrics (Grafana Dashboard #10280)
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/metrics.png" width="900"/> 
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/metrics_rest.png" width="900"/> 

### Prometheus Job Config
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/prometheus_config.png" width="600"/>  

### istio-proxy logging level
```shell
curl -X POST localhost:15000/logging?level=debug
```

### mTLS status
```shell
istioctl x describe pod <<podname>>
```
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/mtls.png" width="500"/> 

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
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/docker_tls.png" width="900"/>  
