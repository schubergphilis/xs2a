FROM jboss/keycloak:3.4.3.Final

USER root

ADD keycloak-provider-extension/keycloak-clientregistration-provider/target/keycloak-clientregistration-provider.jar /opt/jboss/keycloak/providers/keycloak-clientregistration-provider.jar
ENV PROXY_ADDRESS_FORWARDING true
RUN chgrp -R 0 $JBOSS_HOME &&\
chmod -R g+rw $JBOSS_HOME

USER jboss
