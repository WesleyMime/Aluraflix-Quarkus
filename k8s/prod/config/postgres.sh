helm repo add azure-marketplace https://marketplace.azurecr.io/helm/v1/repo

helm install postgres --set auth.database=aluraflix azure-marketplace/postgresql