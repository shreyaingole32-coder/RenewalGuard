# RenewalGuard

**RenewalGuard** is an AI-first enterprise procurement platform. It acts as an
autonomous **AI Procurement Manager**: it discovers and reads every SaaS
contract a company owns, tracks renewal/cancellation windows, benchmarks
vendor pricing against the market, drafts negotiation or cancellation emails,
and asks a human for a single one-click approval before anything is sent.

> AI automates the repetitive work. Humans make the final call. Every
> recommendation ships with transparent reasoning.

---

## Monorepo layout

```
renewalguard/
├── apps/
│   ├── api/          Spring Boot 3.3 (Java 21) — system of record, auth, approvals, audit trail
│   ├── web/           Next.js 15 (TypeScript) — Stripe/Linear/Rippling-grade UI
│   └── agents/        FastAPI (Python) — LLM agents: OCR/clause extraction, pricing
│                       benchmarking, negotiation drafting, renewal-risk scoring
├── packages/
│   ├── ui/            Shared React component library (design system)
│   └── shared/        Shared TypeScript types, constants, utils used by web + ui
├── infrastructure/
│   ├── docker/         Dockerfiles / compose fragments
│   ├── kubernetes/     Base + overlay manifests (dev / prod) via kustomize
│   └── terraform/       VPC, EKS, RDS, S3 modules + per-environment stacks
└── .github/workflows/  CI pipelines per app
```

## Architecture at a glance

```
┌────────────┐      ┌──────────────┐      ┌────────────────────┐
│  apps/web  │◄────►│   apps/api    │◄────►│     PostgreSQL      │
│  Next.js   │ REST │ Spring Boot   │       │  (contracts, audit) │
└────────────┘      │  (System of   │      └────────────────────┘
                     │   Record +    │
                     │   Auth + RBAC)│      ┌────────────────────┐
                     └──────┬────────┘◄────►│      Redis          │
                            │ REST/async     │ (cache, job queue)  │
                            ▼                └────────────────────┘
                     ┌──────────────┐
                     │  apps/agents  │      ┌────────────────────┐
                     │   FastAPI     │◄────►│   S3 / Object store  │
                     │ LLM Agents:   │      │ (contract PDFs/OCR)  │
                     │ - Extraction  │      └────────────────────┘
                     │ - Pricing     │
                     │ - Negotiation │      ┌────────────────────┐
                     │ - Risk scoring│◄────►│  LLM Provider (API)  │
                     └──────────────┘      └────────────────────┘
```

- **apps/api** is the source of truth: contracts, users, orgs, approvals,
  audit logs, RBAC. It orchestrates calls to `apps/agents` and never lets an
  agent take an irreversible action without a human approval record.
- **apps/agents** is stateless-ish and does the actual "AI work": OCR +
  clause extraction, market-price benchmarking, savings estimation,
  negotiation-strategy generation, and email drafting. Every agent response
  includes a `reasoning` field and a `confidence` score — nothing is a black
  box.
- **apps/web** is the operator console: renewal calendar, contract vault,
  savings dashboard, and the one-click approval queue.

## Local development

Prerequisites: Docker + Docker Compose, Node 20+, pnpm 9+, Java 21, Python 3.12+.

```bash
cp .env.example .env
docker compose up -d          # postgres, redis, minio (S3-compatible), mailhog
pnpm install                  # installs web + ui + shared workspaces

# API (Spring Boot)
cd apps/api && ./mvnw spring-boot:run

# Agents (FastAPI)
cd apps/agents && pip install -r requirements.txt && uvicorn app.main:app --reload --port 8001

# Web (Next.js)
cd apps/web && pnpm dev
```

Then visit:
- Web app → http://localhost:3000
- API → http://localhost:8080/api
- Agents service → http://localhost:8001/docs
- Mailhog (fake outbound email for negotiation drafts) → http://localhost:8025
- MinIO console (S3-compatible contract storage) → http://localhost:9001

## Package naming

- Java base package: `com.renewalguard`
- Python package: `renewalguard_agents`
- TypeScript workspace scope: `@renewalguard/*`

## Guiding principles (encoded in the product, not just the pitch deck)

1. **Automation with a leash.** Agents can draft and recommend; only a human
   approval event (`ApprovalDecision`) can trigger an outbound email or a
   cancellation action.
2. **Transparent reasoning.** Every AI-generated recommendation persists a
   `reasoning`, `sources`, and `confidence` alongside the output — visible in
   the UI, not just logged.
3. **Enterprise-grade by default.** RBAC, SSO-ready auth scaffolding,
   encrypted-at-rest contract storage, full audit trail, and multi-tenant
   data isolation from day one.
4. **Savings-first UX.** The dashboard's primary metric is dollars at risk /
   dollars saved — every other view supports that number.

## CI/CD

See `.github/workflows/` — each app has its own pipeline (lint, test, build,
container image push). `infrastructure/kubernetes` + `infrastructure/terraform`
define how those images get deployed.
