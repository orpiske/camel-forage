# Release Forage

To release Forage, first export the variables referencing the current and the next version: 


```shell
export CURRENT_DEVELOPMENT_VERSION=1.1
export NEXT_DEVELOPMENT_VERSION=1.2
```

Then, trigger the Github workflow action that will perform the release:

```shell
gh workflow run release -f currentDevelopmentVersion=1.0 -f nextDevelopmentVersion=1.1
```

The process is completely automated. It may take from a few minutes to as much as 1 hour for the Maven Central publication to happen. 

You can verify the publication status on the [Publishing Page](https://central.sonatype.com/publishing).

