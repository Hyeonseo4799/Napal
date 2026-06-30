# Pomi

**For me, Pomi** — turn your photos into beautifully styled, interactive keepsake cards.

## Overview

Pomi lets you create personalized photo cards — add a name, tags, and a short message to your photo — then reimagine them with AI-powered painting styles. The finished card isn't just an image; it's a small interactive object you can tilt, zoom, and play with.

## Key Features

### 🎨 Custom Card Creation with AI Painting
Pick a photo, personalize it with a name, tags, and a one-line message, then repaint it into a new art style — photorealistic, anime, oil painting, or doodle. Powered by **Gemini 2.5 Flash Image**.

### 📱 Cross-Platform
Built with Kotlin Multiplatform and Compose Multiplatform, Pomi runs natively on both **Android and iOS** from a single shared codebase.

### ✋ Interactive Card Play
Cards aren't static. Pinch to zoom in and out, and drag to tilt the card in 3D for a tactile, hands-on feel.

## Tech Stack

- **Kotlin Multiplatform** & **Compose Multiplatform** — shared UI and business logic across Android and iOS
- **Ktor** — networking client for AI image generation requests
- **Coil3** — async image loading
- **Peekaboo** — cross-platform image picker
- **Gemini 2.5 Flash Image** — AI-powered style transfer
