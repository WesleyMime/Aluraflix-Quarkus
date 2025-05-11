<div align="center">
<h1> Aluraflix </h1>

[![en][en-shield]][en-url]
[![pt-br][pt-br-shield]][pt-br-url]
[![project_license][license-shield]][license-url]
[![last-commit][commit-shield]][commit-url]
![workflow][workflow-shield]
</div>

![demo-api](https://github.com/WesleyMime/Aluraflix-Quarkus/assets/55067868/df82bf93-e363-4fc8-a633-80be31c5f6a5)

## Description

Aluraflix is a REST API that allows users to create playlists with links to their favorite videos, organized by categories.

## Project objectives

The project was developed in sprints lasting 1 week each, which had certain activities to be implemented. For better management of activities, trello was used.

- [Sprint 1 Trello](https://trello.com/b/Mj5x6lMZ/alura-challenge-backend-semana-1)
- [Sprint 2 Trello](https://trello.com/b/5DavhAH7/alura-challenge-backend-semana-2)
- [Sprint 3 Trello](https://trello.com/b/STIogyU7/alura-challenge-backend-semana-3)

## Key Features

- Create, read, update, and delete (CRUD) operations for videos and categories.
- Built with **Quarkus** for high-performance and lightweight Java applications.
- Uses **PostgreSQL** for reliable and scalable relational data storage.
- Fully containerized with **Docker** for easy deployment.
- Orchestrated using **Kubernetes** for scalability and resilience.

## Technologies

![java]
![quarkus]
![swagger]
![postgresql]
![docker]
![kubernetes]
![oracle-cloud]
![cloudflare]

## Deploy

The Quarkus API is running on an Oracle Cloud Infrastructure Virtual Machine, with DNS and HTTPS from Cloudflare.

The link to access is:
* API: https://aluraflix-api.marujo.site

![demo-front](https://github.com/WesleyMime/Aluraflix-Quarkus/assets/55067868/88115b77-3ac9-4885-876d-24a19e6bca65)

## Run locally

To run locally you need to have Docker installed.

- Clone project
```
git clone https://github.com/WesleyMime/Aluraflix-Quarkus.git
```

- Enter the project folder
```
cd Aluraflix-Quarkus
```

- Start services
```
docker compose up
```

### Usage

Once deployed, you can use the API and/or the Front-end to manage playlists and categories.

The links to access are:
- Front-end: http://localhost:8080
- API: http://localhost

## Documentation

The documentation made in Postman can be accessed by this [link](https://documenter.getpostman.com/view/19203694/2s8ZDa2MUK).


## License

Distributed under the MIT license. See `LICENSE.txt` for more information.


[en-shield]: https://img.shields.io/badge/lang-en-green.svg?style=for-the-badge
[en-url]: https://github.com/WesleyMime/Aluraflix-Quarkus/blob/main/README.md
[pt-br-shield]: https://img.shields.io/badge/lang-pt--br-lightdarkgreen.svg?style=for-the-badge
[pt-br-url]: https://github.com/WesleyMime/Aluraflix-Quarkus/blob/main/README.pt-br.md
[commit-shield]: https://img.shields.io/github/last-commit/wesleymime/Adopet.svg?style=for-the-badge
[commit-url]: https://github.com/wesleymime/Aluraflix-Quarkus/commit
[license-shield]: https://img.shields.io/github/license/wesleymime/Aluraflix-Quarkus.svg?style=for-the-badge
[license-url]: https://github.com/wesleymime/Aluraflix-Quarkus/blob/master/LICENSE.txt
[workflow-shield]: https://img.shields.io/github/actions/workflow/status/wesleymime/Aluraflix-Quarkus/main.yml?style=for-the-badge
[workflow-url]: https://img.shields.io/github/actions/workflow/status/wesleymime/Aluraflix-Quarkus/main.yml

[java]: https://img.shields.io/badge/Java-000000?style=for-the-badge&logo=openjdk&logoColor=white
[quarkus]: https://img.shields.io/badge/Quarkus-4695EB?style=for-the-badge&logo=quarkus&logoColor=white
[swagger]: https://img.shields.io/badge/swagger-85EA2D?logo=swagger&logoColor=black&style=for-the-badge
[postgresql]: https://img.shields.io/badge/postgresql-4169E1?style=for-the-badge&logo=postgresql&logoColor=white
[docker]: https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white
[kubernetes]: https://img.shields.io/badge/kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white
[oracle-cloud]: https://custom-icon-badges.demolab.com/badge/Oracle%20Cloud-F80000?logo=oracle&logoColor=white&style=for-the-badge
[cloudflare]: https://img.shields.io/badge/Cloudflare-F38020?logo=Cloudflare&logoColor=white&style=for-the-badge