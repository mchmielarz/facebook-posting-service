FROM iron/java:1.8
MAINTAINER michal chmielarz at g mail dooot com

ENV USER_NAME blogger
ENV APP_HOME /home/$USER_NAME/app

RUN adduser -S $USER_NAME
RUN mkdir $APP_HOME

RUN mkdir $APP_HOME/logs
RUN chown $USER_NAME $APP_HOME/logs

COPY facebook-posting-service*all.jar $APP_HOME/facebook-posting-service.jar
RUN chown $USER_NAME $APP_HOME/facebook-posting-service.jar

USER $USER_NAME
WORKDIR $APP_HOME
RUN sh -c 'touch facebook-posting-service.jar'

CMD java -Djava.security.egd=file:/dev/./urandom \
            -Dfacebook.app.secret=$APP_SECRET \
            -Dfacebook.user.access.token=$ACCESS_TOKEN \
            -Dfacebook.page.id=$PAGE_ID \
            -Dfacebook.app.id=$APP_ID \
            -jar facebook-posting-service.jar
