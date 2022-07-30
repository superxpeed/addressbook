## Dashboard installation

### Install Docker Desktop for Mac
https://www.docker.com/products/docker-desktop/

### Enable Kubernetes

### Give it at least 12 cores and 24GB ram

### brew install ansible

### From here 
https://github.com/kubernetes/dashboard/releases  
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.6.0/aio/deploy/recommended.yaml   

### Create user:
kubectl apply -f dashboard-adminuser.yml

### Create role binding:
kubectl apply -f admin-role-binding.yml

### Create token:
kubectl -n kubernetes-dashboard create token admin-user

### Run script in Terminal:
kubectl proxy

### UI URL:
http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/login

### cd PROJECT_ROOT/devops/ansible
### ansible-playbook delete.yaml
### ansible-playbook deploy.yaml