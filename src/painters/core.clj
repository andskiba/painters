(ns painters.core
  (:require [org.httpkit.client :as http]
            [hickory.core :as h]
            [hickory.select :as s]
            [clojure.java.io :as io]
            [clojure.string :as str])
  (:gen-class))

(defn get-page [url]
  (-> @(http/get url) :body h/parse h/as-hickory))

(defn galleries []
  (let [page (get-page "https://www.audkawa.com/")]
    (into #{}
          (map #(get-in % [:attrs :href])
               (s/select (s/child (s/class "gallery-collection")
                                  (s/tag :a))
                         page)))))

(defn gallery-images [gallery]
  (let [page (get-page (str "https://www.audkawa.com" gallery))]
    (map #(get-in % [:attrs :data-src])
         (s/select (s/child (s/class "slideshow")
                            (s/class "slide")
                            (s/tag :img))
                   page))))

(defn write-file [file-path bytes]
  (with-open [out (io/output-stream (io/file file-path))]
    (.write out bytes)))

(defn url-to-file-name [url]
  (-> (str/split url #"\/") reverse first))

(defn download-image [target-dir url]
  (let [file-name (url-to-file-name url)]
    (write-file (str target-dir file-name)
                (.bytes (:body @(http/get url))))))

(defn download-audrey []
  (doseq [img (mapcat gallery-images (galleries))]
    (download-image "/tmp/painters/" img)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (download-audrey))
