module.exports = {
  mode: "jit",
  purge: ["./public/**/*.html", "./src/**/*.{js,jsx,ts,tsx,vue}"],
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {
      colors: {
        background: "#EAEAEA",
        defaultText: "#525252",
        green: {
          1: "#00BDAA",
        },
        purple: {
          1: "#9865BD",
          2: "#C8ADDC",
        },
        blue: {
          c1: "#46A2E8",
          c2: "#2A63BE",
        },
        yellow: {
          c1: "#D8DE1C",
          c2: "#E3E846",
        },
      },
      screens: {
        sm: "640px",
        md: "768px",
        lg: "1024px",
        xl: "1280px",
      },
    },
  },
  variants: {
    extend: {},
  },
  plugins: [],
};
