FROM iron/java:1.8
MAINTAINER michal chmielarz at g mail dooot com

ENV USER_NAME blogger
ENV APP_HOME /home/$USER_NAME/app

RUN adduser -S $USER_NAME
RUN mkdir $APP_HOME

RUN mkdir $APP_HOME/logs
RUN chown $USER_NAME $APP_HOME/logs

ADD jvm-bloggers-facebook-client*all.jar $APP_HOME/jvm-bloggers-facebook-client.jar
RUN chown $USER_NAME $APP_HOME/jvm-bloggers-facebook-client.jar

USER $USER_NAME
WORKDIR $APP_HOME
RUN sh -c 'touch jvm-bloggers-facebook-client.jar'

CMD java -Djava.security.egd=file:/dev/./urandom -Dfacebook.app.secret=$APP_SECRET -Dfacebook.user.access.token=$ACCESS_TOKEN -jar jvm-bloggers-facebook-client.jar
