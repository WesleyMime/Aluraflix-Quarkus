helm uninstall postgres
helm uninstall nginx-ingress
kubectl delete -f ./config/.
sleep 5