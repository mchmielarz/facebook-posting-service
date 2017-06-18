# Facebook client

## Configuration
The application requires four parameters: identifier of the Facebook page, user access token and application's id and secret.

### Page id
A unique identifier of the Facebook page you would like to post on. Can be found with [findmyfbid.com](http://findmyfbid.com).

### Application id
The application id can be found on the application dashboard on [developers.facebook.com](https://developers.facebook.com/apps).

### Application secret
To get the application secret you have to be in admin role of a page on which you would like to publish posts.

Go to the application's dashboard. The secret is placed on the main page of it.

### Access token
To grab a user access token two calls have to be made to the Facebook Graph API:
* the first one for an authentication code:
`GET https://www.facebook.com/dialog/oauth?client_id={app-id}&redirect_uri={redirect-uri}&scope=manage_pages,publish_pages`
* the second one to change the code to access user token:
`GET https://graph.facebook.com/oauth/access_token?client_id={app-id}&redirect_uri={redirect-uri}&client_secret={app-secret}&code={code}`

`{redirect-uri}` can be anything based on *App Domain* defined on the application settings page.

### Secured requests
Keep in mind that call for a Page Access Token uses appsecret_proof to protect the request. For more information about securing request look [here](https://developers.facebook.com/docs/graph-api/securing-requests).

## Running
You need to **have kafka running** on the address specified in application.conf (by default localhost:9092) before starting the service.

### Docker
To generate a Docker image simply run:
`./gradlew buildDocker`

The application as a Docker container can be started with the following command:
`docker run -e PAGE_ID=... -e APP_SECRET=... -e ACCESS_TOKEN=... docker_image_name`

### IDE
You can start the application from IDE as well.

The main class is `pl.devthoughts.facebook.client.Application`.

Required configuration parameters have to be provided as VM parameters: `-Dfacebook.app.secret=... -Dfacebook.user.access.token=... -Dfacebook.page.id=...`
