addSbtPlugin(
  "org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0" excludeAll(
    // NB: we force a newer scalariform, because of a bug with sbt
    ExclusionRule(organization = "com.danieltrinh"),
    ExclusionRule(organization = "org.scalariform", name = "scalariform_2.10")
    )
)

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")