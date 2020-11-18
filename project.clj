(defproject hermes.storm "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/spec.alpha "0.2.187"]
                 [org.clojure/test.check "1.1.0"]
                 [com.taoensso/timbre "4.10.0"]

                 [org.apache.storm/storm-client "2.2.0"]
                 [org.apache.storm/storm-clojure "2.2.0"]

                 ;; Deps for storm-starter java examples
                 [org.apache.storm/storm-redis "2.2.0"]
                 [org.apache.storm/storm-hdfs "2.2.0"]

                 [commons-collections/commons-collections "3.2.2"]]

  ;; Kept encountering "Tried to use insecure HTTP repository without TLS"
  ;; error from  Leigingen. The docs suggested to do this for those deps :
  :exclusions [net.minidev/json-smart
               com.sun.jersey/jersey-client
               javax.jms/jms]

  :repl-options {:init-ns hermes.storm}

  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]

  :javac-options     ["-target" "11" "-source" "11"]
  :jvm-opts          ["-Xms512m" #_ "-XX:+UseConcMarkSweepGC"]

  :profiles {:dev {:global-vars {*warn-on-reflection* true
                                 *assert*             true}

                   :plugins [;;[lein-virgil "0.1.9"]
                             [venantius/yagni "0.1.7"]
                             [lein-kibit "0.1.6"]
                             [jonase/eastwood "0.3.5"]]}
             :uberjar {:aot :all}}

  :main ^{:skip-aot true} hermes.storm)
