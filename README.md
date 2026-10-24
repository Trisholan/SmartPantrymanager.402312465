# Smart Pantry Manager

**Student:** Trisholan Moodley  
**Student number:** 402312465  
**Module:** Mobile App Development 700

## Overview
Smart Pantry Manager is a Java-based Android application designed to help reduce food waste by tracking pantry ingredients and suggesting recipes that use ingredients available to the user.

## Main features
- Add, view, edit and delete pantry ingredients.
- Store pantry data locally using SQLite.
- Include a collection of 20 preloaded recipes.
- Display suggested recipes when their listed ingredients are present in the pantry.
- View recipe details and preparation methods.
- Validate ingredient form entries.
- Provide a settings screen.

## Technology
- Java
- Android SDK
- SQLite via Android `SQLiteOpenHelper`
- Android Activities and Intents

## Database
SQLite stores pantry ingredients on the device, allowing the app to retain pantry data between sessions without requiring a network connection. Recipe data is preloaded when the database is created.

## Current scope
Recipe matching checks ingredient names and a simplified quantity threshold. Recipe-specific quantities and unit conversions are not fully modelled. The expiry-reminder setting is a display preference and does not schedule notifications.

## Repository
GitHub: https://github.com/Trisholan/SmartPantryManager.new
