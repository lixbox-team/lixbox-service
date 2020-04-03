# lixbox-service

Ce dépôt offre des fonctions supports pour les projets microservices:

* CodeVersionUtil
* WildflyUtil

Il offre aussi un filtre pour contourner les fonctions CORS de HTTP

Le site du service est [ici](https://project-site.service.lixtec.fr/lixbox-service)  


## Dépendences

Sans objet
     

## Utilisateur nécessaire

Sans objet


## Profil accepté par défaut

Sans objet


## Rôles disponibles pour le service

Sans objet
     

## Fonctions d'administration

Sans objet
     

## Fonctions batch

Sans objet


## Policies JAAS

Sans objet


## Database

Sans objet


## CORS filter

1. ajouter une cascade de répertoires: fr/gpmm/wildfly/main dans les modules WILDFLY
2. copier la derniere version du jar 
3. créer un fichier module.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <module xmlns="urn:jboss:module:1.3" name="fr.gpmm.wildfly">
      <resources>
        <resource-root path="<nomdufichier>.jar"/>
      </resources>
      <dependencies>        
        <module name="io.undertow.core"/>
        <module name="org.slf4j"/>
      </dependencies>
    </module> 

4. modifier le subsystem undertow dans le fichier de configuration

    ...
    <filters>      
      <filter name="cors-filter" 
         module="fr.gpmm.wildfly" 
         class-name="fr.lixbox.wildfly.undertow.filter.CorsFilter">
        <param name="acceptedOrigin" value="*"/>
      </filter>
    </filters>
    ...
    <host name="...">
      <filter-ref name="cors-filter"/>
    </host>
    ...