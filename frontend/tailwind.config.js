/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx,ts,tsx}'],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#2E7D32',
          dark: '#1B5E20',
          light: '#81C784',
        },
        earth: '#8D6E63',
        surface: '#F8F9FA',
        cream: '#F4F1EA',
      },
      fontFamily: {
        sans: ['"DM Sans"', 'system-ui', 'sans-serif'],
        display: ['Fraunces', 'Georgia', 'serif'],
      },
      boxShadow: {
        card: '0 4px 24px rgba(27, 94, 32, 0.08)',
        nav: '0 2px 16px rgba(0, 0, 0, 0.06)',
        showcase: '0 25px 50px -12px rgba(27, 94, 32, 0.15)',
      },
    },
  },
  plugins: [],
};
