(ns hermes.storm
  (:import [org.apache.storm StormSubmitter]
           [org.apache.storm.utils Utils])
  (:require
   [org.apache.storm.clojure :as storm]
   [org.apache.storm.config :as storm-config]))

;;------------------------------------------------------------------;;

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

;;------------------------------------------------------------------;;
;;------------------------------------------------------------------;;

(comment

  (import 'hermes.java.storm.Alaki)

  (-> (Alaki/main (into-array ["ali" "bala"])))


  (def segments
    [{:name "ali" :fname "amoodi" :age 28}
     {:name "alo" :fname "amidi" :age 28}
     {:name "ali" :fname "amoodi" :age 28}
     {:name "alo" :fname "amidi" :age 28}])

  (storm/defspout source-stream ["source-stream"]
    [conf context collector]
    (storm/spout
      (nextTuple
        []
        (Thread/sleep 100)
        (storm/emit-spout! collector [(rand-nth segments)]))
      (ack [id])))

  (storm/defbolt transition-bolt ["split-by-fname"]
    [segment collector]
    (let [memory (atom {})]
      (storm/bolt
        (execute
          [segment]
          (let [k   (-> segment :fname)
                age (-> segment :age)]
            (swap! memory
                   (fn [memory]
                     (case (-> memory (get k))
                       true  (update-in memory [k :age] + age)
                       false (assoc-in memory [k :age] age))))

            (storm/emit-bolt! collector [segment @memory] :anchor segment)
            (storm/ack! collector segment))))))

  (defn mk-topology []
    (storm/topology

      {"source" (storm/spout-spec source-stream)}

      {"transition" (storm/bolt-spec {"source" :shuffle}
                                     transition-bolt
                                     :p 5)}))

  (def topology (mk-topology))

  (defn submit-topology! [name]
    (StormSubmitter/submitTopology
      name
      {storm-config/TOPOLOGY-DEBUG   true
       storm-config/TOPOLOGY-WORKERS 10}
      (mk-topology)))

  (submit-topology! "alaki")

)

;;------------------------------------------------------------------;;
;;------------------------------------------------------------------;;
