// vite.config.js
import { defineConfig } from "file:///C:/Users/user/IdeaProjects/Taxi_project/fronted/node_modules/vite/dist/node/index.js";
import react from "file:///C:/Users/user/IdeaProjects/Taxi_project/fronted/node_modules/@vitejs/plugin-react/dist/index.mjs";
import autoprefixer from "file:///C:/Users/user/IdeaProjects/Taxi_project/fronted/node_modules/autoprefixer/lib/autoprefixer.js";
var vite_config_default = defineConfig({
  plugins: [
    react()
    // Vite plugin for React
  ],
  resolve: {
    alias: {
      // Define path aliases for convenience
      "@components": "/src/components",
      "@assets": "/src/assets",
      "@utils": "/src/utils"
    }
  },
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          vendor: ["react", "react-dom", "lodash"]
          // Group common libraries into separate chunks
        }
      }
    },
    chunkSizeWarningLimit: 1e3,
    // Set chunk size warning limit (in KB)
    sourcemap: true
    // Enable source map generation for easier debugging
  },
  assetsInclude: [
    "**/*.png",
    "**/*.PNG",
    "**/*.jpg",
    "**/*.jpeg",
    "**/*.svg",
    "**/*.gif",
    "**/*.WEBP",
    "**/*.webp"
  ],
  server: {
    host: true,
    // Enable access to the dev server from other devices on the network
    port: 5174,
    // Define the port for the dev server
    hmr: {
      overlay: false
      // Disable the HMR overlay to prevent error overlays
    }
  },
  css: {
    postcss: {
      plugins: [
        autoprefixer
        // Automatically add vendor prefixes
      ]
    }
  },
  optimizeDeps: {
    include: [
      "react",
      "react-dom",
      "lodash"
      // Pre-bundle dependencies for faster development
    ]
  },
  define: {
    "process.env": {}
    // Define environment variables if needed
  }
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcuanMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFx1c2VyXFxcXElkZWFQcm9qZWN0c1xcXFxUYXhpX3Byb2plY3RcXFxcZnJvbnRlZFwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcdXNlclxcXFxJZGVhUHJvamVjdHNcXFxcVGF4aV9wcm9qZWN0XFxcXGZyb250ZWRcXFxcdml0ZS5jb25maWcuanNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL0M6L1VzZXJzL3VzZXIvSWRlYVByb2plY3RzL1RheGlfcHJvamVjdC9mcm9udGVkL3ZpdGUuY29uZmlnLmpzXCI7aW1wb3J0IHsgZGVmaW5lQ29uZmlnIH0gZnJvbSAndml0ZSc7XHJcbmltcG9ydCByZWFjdCBmcm9tICdAdml0ZWpzL3BsdWdpbi1yZWFjdCc7XHJcbmltcG9ydCBhdXRvcHJlZml4ZXIgZnJvbSAnYXV0b3ByZWZpeGVyJzsgLy8gU3RhdGlrIGltcG9ydFxyXG5cclxuLy8gRXhwb3J0IHRoZSBWaXRlIGNvbmZpZ3VyYXRpb25cclxuZXhwb3J0IGRlZmF1bHQgZGVmaW5lQ29uZmlnKHtcclxuICBwbHVnaW5zOiBbXHJcbiAgICByZWFjdCgpIC8vIFZpdGUgcGx1Z2luIGZvciBSZWFjdFxyXG4gIF0sXHJcbiAgcmVzb2x2ZToge1xyXG4gICAgYWxpYXM6IHtcclxuICAgICAgLy8gRGVmaW5lIHBhdGggYWxpYXNlcyBmb3IgY29udmVuaWVuY2VcclxuICAgICAgJ0Bjb21wb25lbnRzJzogJy9zcmMvY29tcG9uZW50cycsXHJcbiAgICAgICdAYXNzZXRzJzogJy9zcmMvYXNzZXRzJyxcclxuICAgICAgJ0B1dGlscyc6ICcvc3JjL3V0aWxzJyxcclxuICAgIH0sXHJcbiAgfSxcclxuICBidWlsZDoge1xyXG4gICAgcm9sbHVwT3B0aW9uczoge1xyXG4gICAgICBvdXRwdXQ6IHtcclxuICAgICAgICBtYW51YWxDaHVua3M6IHtcclxuICAgICAgICAgIHZlbmRvcjogWydyZWFjdCcsICdyZWFjdC1kb20nLCAnbG9kYXNoJ10sIC8vIEdyb3VwIGNvbW1vbiBsaWJyYXJpZXMgaW50byBzZXBhcmF0ZSBjaHVua3NcclxuICAgICAgICB9LFxyXG4gICAgICB9LFxyXG4gICAgfSxcclxuICAgIGNodW5rU2l6ZVdhcm5pbmdMaW1pdDogMTAwMCwgLy8gU2V0IGNodW5rIHNpemUgd2FybmluZyBsaW1pdCAoaW4gS0IpXHJcbiAgICBzb3VyY2VtYXA6IHRydWUsIC8vIEVuYWJsZSBzb3VyY2UgbWFwIGdlbmVyYXRpb24gZm9yIGVhc2llciBkZWJ1Z2dpbmdcclxuICB9LFxyXG4gIGFzc2V0c0luY2x1ZGU6IFtcclxuICAgICcqKi8qLnBuZycsXHJcbiAgICAnKiovKi5QTkcnLFxyXG4gICAgJyoqLyouanBnJyxcclxuICAgICcqKi8qLmpwZWcnLFxyXG4gICAgJyoqLyouc3ZnJyxcclxuICAgICcqKi8qLmdpZicsXHJcbiAgICAnKiovKi5XRUJQJyxcclxuICAgICcqKi8qLndlYnAnXHJcbiAgXSxcclxuICBzZXJ2ZXI6IHtcclxuICAgIGhvc3Q6IHRydWUsIC8vIEVuYWJsZSBhY2Nlc3MgdG8gdGhlIGRldiBzZXJ2ZXIgZnJvbSBvdGhlciBkZXZpY2VzIG9uIHRoZSBuZXR3b3JrXHJcbiAgICBwb3J0OiA1MTc0LCAvLyBEZWZpbmUgdGhlIHBvcnQgZm9yIHRoZSBkZXYgc2VydmVyXHJcbiAgICBobXI6IHtcclxuICAgICAgb3ZlcmxheTogZmFsc2UsIC8vIERpc2FibGUgdGhlIEhNUiBvdmVybGF5IHRvIHByZXZlbnQgZXJyb3Igb3ZlcmxheXNcclxuICAgIH0sXHJcbiAgfSxcclxuICBjc3M6IHtcclxuICAgIHBvc3Rjc3M6IHtcclxuICAgICAgcGx1Z2luczogW1xyXG4gICAgICAgIGF1dG9wcmVmaXhlciAvLyBBdXRvbWF0aWNhbGx5IGFkZCB2ZW5kb3IgcHJlZml4ZXNcclxuICAgICAgXSxcclxuICAgIH0sXHJcbiAgfSxcclxuICBvcHRpbWl6ZURlcHM6IHtcclxuICAgIGluY2x1ZGU6IFtcclxuICAgICAgJ3JlYWN0JyxcclxuICAgICAgJ3JlYWN0LWRvbScsXHJcbiAgICAgICdsb2Rhc2gnIC8vIFByZS1idW5kbGUgZGVwZW5kZW5jaWVzIGZvciBmYXN0ZXIgZGV2ZWxvcG1lbnRcclxuICAgIF0sXHJcbiAgfSxcclxuICBkZWZpbmU6IHtcclxuICAgICdwcm9jZXNzLmVudic6IHt9IC8vIERlZmluZSBlbnZpcm9ubWVudCB2YXJpYWJsZXMgaWYgbmVlZGVkXHJcbiAgfSxcclxufSk7XHJcbiJdLAogICJtYXBwaW5ncyI6ICI7QUFBMlUsU0FBUyxvQkFBb0I7QUFDeFcsT0FBTyxXQUFXO0FBQ2xCLE9BQU8sa0JBQWtCO0FBR3pCLElBQU8sc0JBQVEsYUFBYTtBQUFBLEVBQzFCLFNBQVM7QUFBQSxJQUNQLE1BQU07QUFBQTtBQUFBLEVBQ1I7QUFBQSxFQUNBLFNBQVM7QUFBQSxJQUNQLE9BQU87QUFBQTtBQUFBLE1BRUwsZUFBZTtBQUFBLE1BQ2YsV0FBVztBQUFBLE1BQ1gsVUFBVTtBQUFBLElBQ1o7QUFBQSxFQUNGO0FBQUEsRUFDQSxPQUFPO0FBQUEsSUFDTCxlQUFlO0FBQUEsTUFDYixRQUFRO0FBQUEsUUFDTixjQUFjO0FBQUEsVUFDWixRQUFRLENBQUMsU0FBUyxhQUFhLFFBQVE7QUFBQTtBQUFBLFFBQ3pDO0FBQUEsTUFDRjtBQUFBLElBQ0Y7QUFBQSxJQUNBLHVCQUF1QjtBQUFBO0FBQUEsSUFDdkIsV0FBVztBQUFBO0FBQUEsRUFDYjtBQUFBLEVBQ0EsZUFBZTtBQUFBLElBQ2I7QUFBQSxJQUNBO0FBQUEsSUFDQTtBQUFBLElBQ0E7QUFBQSxJQUNBO0FBQUEsSUFDQTtBQUFBLElBQ0E7QUFBQSxJQUNBO0FBQUEsRUFDRjtBQUFBLEVBQ0EsUUFBUTtBQUFBLElBQ04sTUFBTTtBQUFBO0FBQUEsSUFDTixNQUFNO0FBQUE7QUFBQSxJQUNOLEtBQUs7QUFBQSxNQUNILFNBQVM7QUFBQTtBQUFBLElBQ1g7QUFBQSxFQUNGO0FBQUEsRUFDQSxLQUFLO0FBQUEsSUFDSCxTQUFTO0FBQUEsTUFDUCxTQUFTO0FBQUEsUUFDUDtBQUFBO0FBQUEsTUFDRjtBQUFBLElBQ0Y7QUFBQSxFQUNGO0FBQUEsRUFDQSxjQUFjO0FBQUEsSUFDWixTQUFTO0FBQUEsTUFDUDtBQUFBLE1BQ0E7QUFBQSxNQUNBO0FBQUE7QUFBQSxJQUNGO0FBQUEsRUFDRjtBQUFBLEVBQ0EsUUFBUTtBQUFBLElBQ04sZUFBZSxDQUFDO0FBQUE7QUFBQSxFQUNsQjtBQUNGLENBQUM7IiwKICAibmFtZXMiOiBbXQp9Cg==
