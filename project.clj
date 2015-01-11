 (defproject recognize-digits "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [incanter/incanter-core "1.9.0"]
                 [incanter/incanter-io "1.9.0"]
                 [incanter/incanter-charts "1.9.0"]
                 [org.clojure/data.csv "0.1.2"]
                 [clatrix "0.4.0"]
                 [net.mikera/vectorz-clj "0.28.0"]
                 [org.clojure/tools.namespace "0.2.8"]
                 [com.univocity/univocity-parsers "1.3.0"]
		[incanter-gorilla "0.1.0"]  
               ]
  :plugins [[lein-gorilla "0.3.5-SNAPSHOT" ]
[cider/cider-nrepl "0.8.2"]

]

:jvm-opts ["-Xmx2g" "-server"]
:profiles {:dev {:dependencies [[alembic "0.3.2"]]}}
 )
