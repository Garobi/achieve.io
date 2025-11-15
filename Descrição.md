# Achieve.io — Documento de Análise Simplificado

## Nome do Sistema e Propósito

**Nome:** Achieve.io
**Propósito:** Plataforma onde jogadores criam, compartilham e competem em **conquistas** e **desafios** de jogos. A comprovação é feita por **vídeo** (Provas) e passa por **verificação** comunitária, oferecendo objetivos para jogos sem achievements e estimulando a competição saudável.

> **Entidades consideradas nesta entrega:** **Usuário, Jogo, Conquista, Desafio, Provas** (conforme diagrama enviado).

---

## Responsabilidades da Equipe

- **Diogo Sagiorato de Oliveira — Autenticação & Segurança:** login/JWT, perfis de acesso, proteção de rotas e políticas de moderação.
- **Davi Alves Pedroso — Núcleo (Jogos, Conquistas, Desafios):** CRUD de **Jogo**, **Conquista** (votos, contadores, vínculo com jogo) e **Desafio** (agrupa conquistas e jogos), validações e regras de negócio.
- **Gabriel Rodrigues Bittencourt — Submissões & Verificação:** **Provas** (upload/link de vídeo), fila de verificação, aprovação/reprovação, atualização de contadores e rankings derivados.

---

## Requisitos Funcionais (alinhados às entidades)

1. **Usuário**

   - RF-01: Cadastro, login (JWT) e gerenciamento de perfil (nome, foto, e-mail, senha).
   - RF-02: Exibir no perfil os totais: **conquistas finalizadas/verificadas** e **desafios finalizados/verificados**.

2. **Jogo**

   - RF-03: CRUD de **Jogo** com nome e plataformas; prevenção de duplicatas.

3. **Conquista**

   - RF-04: Criar/editar **Conquista** vinculada a um **Jogo** com nome, descrição, autor, **votos**, **conclusões (total)** e **verificado (bool)**.
   - RF-05: Listar/filtrar conquistas por jogo, popularidade (votos) e status de verificação.

4. **Desafio**

   - RF-06: Criar/editar **Desafio** com nome, descrição, autor, **votos**, **conclusões**, **verificado (bool)**, vínculo a **Conquistas** (N:N) e, quando aplicável, a **Jogos**.
   - RF-07: Publicar/arquivar desafio; exibir página do desafio com suas conquistas relacionadas.

5. **Provas (Submissões de Vídeo)**

   - RF-08: Enviar **Prova** (vídeo/link) vinculando **Usuário** + **Conquista**; registrar status (pendente/aprovado/reprovado) e justificativa.
   - RF-09: **Fila de verificação**: aprovadores analisam o vídeo e decidem; ao aprovar, atualizar contadores de conclusões/verificados na **Conquista**, **Desafio** (se aplicável) e **Usuário**.

6. **Interação & Ranking (derivado)**

   - RF-10: Permitir **votos** em Conquista e Desafio (1 voto por usuário).
   - RF-11: Exibir **rankings derivados** por Conquista/Desafio (ordenação por tempo/data de aprovação ou métrica definida na própria conquista — quando existir), sem persistência obrigatória nesta fase.

---

## Requisitos Não Funcionais

1. **Segurança:** JWT, senhas com hash, controle de papéis (usuário, verificador, moderador), rate limiting em criação e envio de Provas.
2. **Qualidade:** validações server-side para todos os campos obrigatórios; tratamento global de erros com respostas padronizadas; logs de auditoria para aprovações.
3. **Banco de Dados:** PostgreSQL com migrações; chaves/índices garantindo integridade dos vínculos (ex.: Provas → Usuário/Conquista; Conquista → Jogo; Desafio ↔️ Conquista).
4. **Desempenho:** paginação em listagens; cache simples para páginas populares (conquistas/desafios mais votados); processamento assíncrono de metadados do vídeo (thumbs, duração).
5. **Usabilidade:** interface responsiva e intuitiva, com tema escuro padrão; formulários com ajuda/contexto sobre regras de submissão.
6. **Armazenamento de Mídia:** suporte a upload multipart ou link (YouTube/Twitch), tamanho máximo configurável e verificação básica de formato.
7. **Manutenibilidade:** arquitetura em camadas (Controller/Service/Repository), DTOs, serviços coesos, documentação mínima dos endpoints (Swagger/OpenAPI).

---

**Equipe:** Diogo Sagiorato de Oliveira · Davi Alves Pedroso · Gabriel Rodrigues Bittencourt · Kaue Elias
**Projeto:** Achieve.io — Plataforma de Conquistas e Desafios com Provas em Vídeo.
