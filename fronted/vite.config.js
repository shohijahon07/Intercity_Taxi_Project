import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import autoprefixer from 'autoprefixer'; // Statik import

// Export the Vite configuration
export default defineConfig({
  plugins: [
    react() // Vite plugin for React
  ],
  resolve: {
    alias: {
      // Define path aliases for convenience
      '@components': '/src/components',
      '@assets': '/src/assets',
      '@utils': '/src/utils',
    },
  },
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          vendor: ['react', 'react-dom', 'lodash'], // Group common libraries into separate chunks
        },
      },
    },
    chunkSizeWarningLimit: 1000, // Set chunk size warning limit (in KB)
    sourcemap: true, // Enable source map generation for easier debugging
  },
  assetsInclude: [
    '**/*.png',
    '**/*.PNG',
    '**/*.jpg',
    '**/*.jpeg',
    '**/*.svg',
    '**/*.gif',
    '**/*.WEBP',
    '**/*.webp'
  ],
  server: {
    host: true, // Enable access to the dev server from other devices on the network
    port: 5174, // Define the port for the dev server
    hmr: {
      overlay: false, // Disable the HMR overlay to prevent error overlays
    },
  },
  css: {
    postcss: {
      plugins: [
        autoprefixer // Automatically add vendor prefixes
      ],
    },
  },
  optimizeDeps: {
    include: [
      'react',
      'react-dom',
      'lodash' // Pre-bundle dependencies for faster development
    ],
  },
  define: {
    'process.env': {} // Define environment variables if needed
  },
});
