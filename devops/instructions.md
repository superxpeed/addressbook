## Dashboard installation

### Install Docker Desktop for Mac
```shell
https://www.docker.com/products/docker-desktop/
```

### Enable Kubernetes

### Give it at least 12 cores and 24GB ram

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
```
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

### Deploy project
```shell
cd $PROJECT_ROOT/devops/ansible
ansible-playbook delete.yaml
ansible-playbook deploy.yaml
```
