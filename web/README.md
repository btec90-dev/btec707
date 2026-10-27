# Mon Parcours FIV · Web App (React + Vite + Tailwind CSS)

This directory contains a fully functioning, responsive web application version of Fatou's IVF Companion & Obstetric Journey app, built with **React**, **Vite**, and **Tailwind CSS**.

## 🚀 Quick Start / Local Development

1. Open a terminal in the `web` folder:
   ```bash
   cd web
   npm install
   ```
2. Run the development server:
   ```bash
   npm run dev
   ```
3. Open `http://localhost:5173` in your browser.

## 📦 Static Deployment (Vercel / Netlify)

To build the static production bundle for hosting on Vercel, Netlify, or any static web server:

1. Run the build command:
   ```bash
   npm run build
   ```
2. The production-ready files will be generated in the `dist/` directory.
3. Deploy the `dist/` folder directly to Vercel (`vercel`) or Netlify (`netlify deploy --prod --dir=dist`).
