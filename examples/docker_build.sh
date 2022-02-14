#!/usr/bin/env bash

push_flag='false'
registry='public.ecr.aws/d8i9k4x3'

print_usage() {
  printf "Usage: docker_build.sh [-p] [-r REGISTRY_NAME]\n"
}

while getopts 'pr:' flag; do
  case "${flag}" in
    p) push_flag='true' ;;
    r) registry="${OPTARG}" ;;    
    *) print_usage
       exit 1 ;;
  esac
done

echo "Logging in to ECR..."
aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin public.ecr.aws/d8i9k4x3

# docker build --no-cache=true -t "$registry/teastore-base" ../utilities/tools.descartes.teastore.dockerbase/
# perl -i -pe's|.*FROM descartesresearch/|FROM '"$registry"'/|g' ../services/tools.descartes.teastore.*/Dockerfile
docker build -t "${registry}/fso-lab-teastore-registry" ../services/tools.descartes.teastore.registry/
docker build -t "${registry}/fso-lab-teastore-persistence" ../services/tools.descartes.teastore.persistence/
docker build -t "${registry}/fso-lab-teastore-image" ../services/tools.descartes.teastore.image/
docker build -t "${registry}/fso-lab-teastore-webui" ../services/tools.descartes.teastore.webui/
docker build -t "${registry}/fso-lab-teastore-auth" ../services/tools.descartes.teastore.auth/
docker build -t "${registry}/fso-lab-teastore-recommender" ../services/tools.descartes.teastore.recommender/
docker build -t "${registry}/fso-lab-teastore-loadgen" ./jmeter/docker/
docker build -t "${registry}/fso-lab-teastore-orderprocessor" ../services/tools.descartes.teastore.orderprocessor/
# perl -i -pe's|.*FROM '"$registry"'/|FROM descartesresearch/|g' ../services/tools.descartes.teastore.*/Dockerfile

if [ "$push_flag" = 'true' ]; then
  # docker push "$registry/teastore-base"  

  docker push "$registry/fso-lab-teastore-registry"
  docker push "$registry/fso-lab-teastore-persistence"
  docker push "$registry/fso-lab-teastore-image"
  docker push "$registry/fso-lab-teastore-webui"
  docker push "$registry/fso-lab-teastore-auth"
  docker push "$registry/fso-lab-teastore-recommender"
  docker push "$registry/fso-lab-teastore-loadgen"
  docker push "$registry/fso-lab-teastore-orderprocessor"
fi