(defproject painters "0.1.0-SNAPSHOT"
  :description "Download images of paintings from painters' websites."
  :url "http://github.com/andskiba/painters"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.2.0"]]
  :main ^:skip-aot painters.core
  :target-path "target/%s"
  :profiles {:dev {:dependencies [[alembic "0.1.0"]]}
             :uberjar {:aot :all}})
