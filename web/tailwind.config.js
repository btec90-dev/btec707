/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        goldPrimary: '#5C3B24',
        goldSecondary: '#8D6648',
        inkText: '#2B2320',
        mutedText: '#6E6058',
        successGreen: '#2B7853',
        warmBg: '#FAF6F0',
        borderLine: '#E2D3C4',
      },
    },
  },
  plugins: [],
}
