<div align="center">
<h1> Aluraflix </h1>

[![en][en-shield]][en-url]
[![pt-br][pt-br-shield]][pt-br-url]
[![project_license][license-shield]][license-url]
[![last-commit][commit-shield]][commit-url]
![workflow][workflow-shield]
</div>

![demo-api](https://github.com/WesleyMime/Aluraflix-Quarkus/assets/55067868/df82bf93-e363-4fc8-a633-80be31c5f6a5)

## Descrição

Aluraflix é uma API REST que permite aos usuários criar playlists com links para seus vídeos favoritos, organizados por categorias.

## Objetivos do projeto

O projeto foi desenvolvido em sprints com duração 1 semana cada, que possuiam determinadas atividades a serem implementadas. Para uma melhor gestão das atividades, foi utilizado o trello como ferramenta.

- [Trello da Sprint 1](https://trello.com/b/Mj5x6lMZ/alura-challenge-backend-semana-1)
- [Trello da Sprint 2](https://trello.com/b/5DavhAH7/alura-challenge-backend-semana-2)
- [Trello da Sprint 3](https://trello.com/b/STIogyU7/alura-challenge-backend-semana-3)

## Características principais

- Operações de criar, ler, atualizar e excluir (CRUD) para vídeos e categorias.
- Construído com **Quarkus** para aplicativos Java leves e de alto desempenho.
- Usa **PostgreSQL** para armazenamento de dados relacionais confiáveis e escaláveis.
- Totalmente contêinerzado com **Docker** para facilitar a implantação.
- Orquestrado usando **Kubernetes** para escalabilidade e resiliência.

## Tecnologias

![java]
![quarkus]
![swagger]
![postgresql]
![docker]
![kubernetes]
![oracle-cloud]
![cloudflare]

## Deploy

A API Quarkus está numa máquina virtual da Oracle Cloud Infrastructure, com DNS e HTTPS da Cloudflare.

O link para acessar é:
* API: https://aluraflix-api.marujo.site 

![demo-front](https://github.com/WesleyMime/Aluraflix-Quarkus/assets/55067868/88115b77-3ac9-4885-876d-24a19e6bca65)

## Rode Localmente

Para rodar é necessário ter Docker instalado.

- Clone o projeto
```
git clone https://github.com/WesleyMime/Aluraflix-Quarkus.git
```

- Entre na pasta do projeto
```
cd Aluraflix-Quarkus
```

- Inicie os serviços
```
docker compose up
```

### Uso

Depois que o aplicativo estiver em execução, você pode usar a API e/ou o Front-end para gerenciar as listas de reprodução e categorias.

Os links para o acesso são:
- Front-end: http://localhost:8080
- API: http://localhost

## Documentação

A documentação feita no Postman pode ser acessada por esse [link](https://documenter.getpostman.com/view/19203694/2s8ZDa2MUK).

## Licença

Distribuído sob a licença do MIT. Consulte `LICENSE.txt` para obter mais informações.


[en-shield]: https://img.shields.io/badge/lang-en-green.svg?style=for-the-badge
[en-url]: https://github.com/WesleyMime/Aluraflix-Quarkus/blob/main/README.md
[pt-br-shield]: https://img.shields.io/badge/lang-pt--br-lightdarkgreen.svg?style=for-the-badge
[pt-br-url]: https://github.com/WesleyMime/Aluraflix-Quarkus/blob/main/README.pt-br.md
[commit-shield]: https://img.shields.io/github/last-commit/wesleymime/Aluraflix-Quarkus.svg?style=for-the-badge
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