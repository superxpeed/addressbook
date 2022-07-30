## Dashboard installation

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

### mac-mini token:
eyJhbGciOiJSUzI1NiIsImtpZCI6IkhWR096cGt5aDJQTDFXWmdrQWk5VkNjR3E0Vzl3cV9lX1ZidjVVSHRkTGMifQ.eyJhdWQiOlsiaHR0cHM6Ly9rdWJlcm5ldGVzLmRlZmF1bHQuc3ZjLmNsdXN0ZXIubG9jYWwiXSwiZXhwIjoxNjU5MTM1NzYwLCJpYXQiOjE2NTkxMzIxNjAsImlzcyI6Imh0dHBzOi8va3ViZXJuZXRlcy5kZWZhdWx0LnN2Yy5jbHVzdGVyLmxvY2FsIiwia3ViZXJuZXRlcy5pbyI6eyJuYW1lc3BhY2UiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsInNlcnZpY2VhY2NvdW50Ijp7Im5hbWUiOiJhZG1pbi11c2VyIiwidWlkIjoiOGRhOTQ0OWMtNjkyMC00NGY2LWI1ZWItOWEzNzg3MTk5MzQyIn19LCJuYmYiOjE2NTkxMzIxNjAsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlcm5ldGVzLWRhc2hib2FyZDphZG1pbi11c2VyIn0.j8mdZqsdpkEuHexixJ36Uv5ufQ1VoIYN1WXnd-7WNHsx0Iz2vQmcCq8CVwz6Kt9nYWqJa_VPmUgTBV4Wv4Zz4AjyvJAU_b7V4wQhvYi21qWzdEFY_3qqbjS57KAggjJ0GGPi2HfsbeZ940z-mrLgsGU7CDJTZiY_XPqXo0lyeX9usbPoeuOK8jeu1ZtBVHOsmHaFvrVXjfDjPZDJ-9Bujy-kGlrP-CoPf1GwIBXbrWei1ss6QICr0KfS8XFPj50kEMparhXL2pdqmQRNnufovvMCo7FLAUBcuSYo1U93I8zDeJFUCrrRF2cYYtz8sn5_lMaISniYb4L7oG1iPwF30Q

### cd ansible
### ansible-playbook delete.yaml
### ansible-playbook deploy.yaml