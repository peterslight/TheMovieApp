# TMDB App

This is a movie app project for Mvp-Match, 

### Brief
Android app which will enable the user to search for movies/shows using The Movie Db API, pick their favorites and hide those they do not wish to see or have already seen.

### Core Task
- All of the information should be retrieved from TMDb APIs
- By default the main screen should show movies and shows the user has added to their favorites list (title, movie poster, description and rating)
- Implement an option to search for a specific movie/show
- Once a user clicks on a movie/show a screen containing title, description, rating, movie/show poster should be shown
- Implement an option to add the movie/show to favorites list (this should persist on app restart)
- Implement an option to hide movie/show form future search results (this should persist on app restart)
- User should be informed if the internet connection is lost

### Download Apk
https://github.com/peterslight/TheMovieApp/blob/main/app/release/Peterstev-Match.apk

### Apk folder
https://github.com/peterslight/TheMovieApp/blob/main/app/release

### How To Run
- clone project
- signup with [TheMovieDb](https://themoviedb.org), then head to settings -> Api.
- copy the 'API Key (v3 auth)' key
- Inside the project open the `SearchRepositoryImpl` file. you have 2 options 
1. `FAST` add the api key directly in the `service.searchMovie::apiKey` param.
2. `RECOMMENDED` add the api key to your environmemnt variables, and call it using `System.getenv(yourApiKey)` find out how to add environment variables [here](https://chlee.co/how-to-setup-environment-variables-for-windows-mac-and-linux/)

- Build/Sync gradle files to download dependencies. NB project uses `JDK-11` and the min android sdk is `Sdk-24`

### Tech Stack
This project follows a modularized approach:

| Option          | Tech                   |
|-----------------|------------------------|
| Language        | Kotlin                 |
| Arch Pattern    | MVVM                   |
| Network         | Retrofit               |
| Android Arch    | Single Activity Nav    |
| Theme           | Material3              |
| Converter       | GSON                   |
| Network         | Retrofit/OkHttp        |
| Db              | Room Db                |
| MultiThreading  | RxKotlin               |
| Dependency Injection | Dagger            |
| Imaging         | Glide                  |
| Tests           | Spek Framework         |
| Mock            | Mockk                  |
| JDK             | JDK 11                 |

Tests can be found in respective module tests packages

## License

[MIT](https://choosealicense.com/licenses/mit/)
