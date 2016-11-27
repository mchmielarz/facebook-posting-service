#Facebook client

[![Build Status](https://travis-ci.org/jvm-bloggers/jvm-bloggers-facebook-client.svg?branch=master)](https://travis-ci.org/jvm-bloggers/jvm-bloggers-facebook-client)

##Configuration

The application requires two parameters at startup: application secret and user access token.

###Application secret
To get the application secret you have to be in admin role of [JVM Bloggers on Facebook](https://www.facebook.com/jvmbloggers).

Go to [the application dashboard](https://developers.facebook.com/apps/984518258323702/dashboard/). The secret is placed on the main page of it. 

###Access token
To grab a user access token two calls have to be made to Facebook Graph API:
* the first one for an authentication code:
`GET https://www.facebook.com/dialog/oauth?client_id={app-id}&redirect_uri={redirect-uri}&scope=manage_pages,publish_pages`
* the second one to change the code to access user token:
`GET https://graph.facebook.com/oauth/access_token?client_id={app-id}&redirect_uri={redirect-uri}&client_secret={app-secret}&code={code}`

`{app-id}` can be found on the application dashboard.

`{redirect-uri}` can be anything based on *App Domain* defined on [the application settings page](https://developers.facebook.com/apps/984518258323702/settings/). 

##Running

You need to **have kafka running** on the address specified in application.conf (by default localhost:9092) before starting the service.

###Docker
To generate a Docker image simply run:
`./gradlew buildDocker`
The application as a Docker container can be started with the following command:
`docker run -e APP_SECRET=... -e ACCESS_TOKEN=... docker_image_name`

###IDE
You can start the application from IDE as well.

The main class is `com.jvm_bloggers.facebook.client.Application`.

Two VM parameters have to be provided: `-Dfacebook.app.secret=...` and `-Dfacebook.user.access.token=...`