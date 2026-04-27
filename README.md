# 🚀 Git Workflow – Linee Guida del Progetto

Questo progetto utilizza un workflow basato su due branch principali:

```text
main  → produzione (stabile)
dev   → sviluppo condiviso
```

---

## 🌳 Struttura dei Branch

```text
main
└── dev
    ├── feature/nome-feature
    └── bugfix/nome-bugfix
```

---

## 📁 Struttura del progetto

Il repository è organizzato come monorepo:

```text
repo
├── .mvn/wrapper
├── backend
├── frontend
├── .gitattributes
├── .gitignore
└── README.md
```

### Backend

La cartella `backend/` contiene tutto il codice relativo al backend.

Qui vanno inserite solo modifiche riguardanti:

* API
* logica server
* controller
* service
* repository
* database
* configurazioni Spring Boot

Esempi di branch backend:

```bash
git checkout -b feature/backend-login
git checkout -b bugfix/backend-auth-error
```

---

### Frontend

La cartella `frontend/` contiene tutto il codice relativo al frontend.

Qui vanno inserite solo modifiche riguardanti:

* pagine Angular
* componenti
* servizi frontend
* routing
* template HTML
* stili CSS/SCSS

Esempi di branch frontend:

```bash
git checkout -b feature/frontend-login-page
git checkout -b bugfix/frontend-navbar-error
```

---

## ⚠️ Regola importante sul monorepo

Ogni branch deve modificare solo la parte interessata:

* modifiche backend → cartella `backend/`
* modifiche frontend → cartella `frontend/`

Se una feature richiede sia backend che frontend, usare un nome chiaro:

```bash
git checkout -b feature/fullstack-nome-feature
```

In quel caso la Pull Request deve spiegare chiaramente quali modifiche sono state fatte su backend e frontend.

---

## 🌱 Creazione di un nuovo branch

Prima di tutto assicurati di essere aggiornato:

```bash
git checkout dev
git pull origin dev
```

### ▶️ Nuova feature

```bash
git checkout -b feature/nome-feature
```

Esempio:

```bash
git checkout -b feature/login-page
```

---

### 🐞 Bugfix

```bash
git checkout -b bugfix/nome-bug
```

Esempio:

```bash
git checkout -b bugfix/fix-login-error
```

---

## 💾 Salvare le modifiche (commit)

```bash
git add . (per aggiungere se necessario)
git commit -m "descrizione chiara delle modifiche"
```

---

## ☁️ Push del branch

```bash
git push origin nome-branch
```

Esempio:

```bash
git push origin feature/login-page
```

---

## 🔁 Pull Request

Dopo il push:

1. Vai su GitHub
2. Apri una **Pull Request**
3. Seleziona:

   ```text
   base: dev
   compare: tuo-branch
   ```

---

## ✅ Merge

* Le Pull Request verso `dev` vengono revisionate
* Il merge viene gestito secondo le regole del progetto (tipicamente dal maintainer)

---

## 🚀 Rilascio in produzione

Quando lo sviluppo è completo:

```text
Pull Request: dev → main
```

Solo codice stabile deve arrivare su `main`.

---

## ⚠️ Buone pratiche

* ❌ NON fare commit direttamente su `dev`
* ❌ NON lavorare su `main`
* ✅ Usa nomi chiari per i branch
* ✅ Fai commit piccoli e descrittivi
* ✅ Aggiorna spesso il tuo branch con `dev`

---

## 🔄 Aggiornare il tuo branch

Se `dev` è cambiato:

```bash
git checkout dev
git pull origin dev

git checkout tuo-branch
git merge dev -> significa portare gli aggiornamenti di dev dentro il tuo branch
```

---

## 🧠 Esempio completo

```bash
git checkout dev
git pull origin dev

git checkout -b feature/login

# lavoro...
git add .
git commit -m "aggiunta login"

git push origin feature/login
```

Poi:
→ apri Pull Request su GitHub

---

## 👨‍💻 Nota finale

Se hai dubbi:

* chiedi prima di fare merge
* evita di rompere `dev` 😄

---
