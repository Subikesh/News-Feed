# News-Feed
A java console application to view and follow everyday news.
Start the application by running [NewsFeedApplication](src/main/java/newsfeed/NewsFeedApplication.java).

The java documentation for news-feed application - [Documentation](https://subikesh.github.io/News-Feed/)

## Application features

### Using NewsAPI
* Make API calls to newsapi to get the latest news
* Display breaking news to the user
* View full details of separate news
* Redirect to the news article page in browser
* News based on particular search tag
* News based on a category - business, entertainment, general
* Search news by sources(NDTV, ESPN…)
* View news results in different countries
* Get top news in a date range
* Implement subscribe feature to let user follow some sources
* Provide user specific news feed based on his subscriptions

### User
* Save any news for offline view
* Use local files to store offline news in the form of serialized objects
* Bookmark any news
* Register new user


## User input instructions

* Enter the numbers from the menu options to perform corresponding actions.
* For the menus with (command: xxxx) type xxxx followed by the option number
* For example: 
  ```
  User (command: user)
  1. Logout
  2. Profile
  ```
  Type "user 2" to open user profile.
* To exit or go back to the previous menu press 0

## Main menu actions details

### Main menu
```
1. Top Headlines
2. Advanced filter options
3. View news sources
4. How to use the interface?
```
1. Top headlines - View the latest headlines and breaking news in english and apply filters on those news.
2. Advanced filter options - View all the news with advanced filters like domains, or sort the news differently.
3. View news sources - View the list of all the sources from which news are taken from
4. How to use the interface? - Redirects to this markdown with user instructions

### User menu
```
1. Login
2. Register
3. View Profile
4. Logout
5. Delete user
```
1. Login - login user with user credentials
2. Regsiter - Create a new user
3. User profile - View user details and manage his/her's subscriptions and bookmarks
4. Logout - Logout the currently logged in user
5. Delete user - Deletes the currently logged in user and deletes files related to that user
