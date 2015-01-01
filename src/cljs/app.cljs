(ns colws.app
  (:use-macros [c2.util :only [bind! pp]])
  (:use [c2.core :only [unify]])
  (:require [c2.scale :as scale]
            [cljs.reader :as reader]
            [goog.net.XhrIo :as xhr]))

(def world
  (atom {}))

(def default-size 4)
(def default-width  (/ 960 default-size))
(def default-height (/ 400 default-size))
(def default-interval 100)

(comment bind-html!
  (bind! "#world"
    [:section#world
     (let [{board :board, height :height, width :width, age :age} @world
           cell-size  default-size]
       [:div#board
        (unify board
          (fn [row]
            [:div.row
             {:style {:width  (* width  cell-size)
                      :height cell-size}}
             (unify
               (filter identity (map-indexed (fn [x a] (if a x)) row))
               (fn [x]
                 [:div.cell.alive
                  {:style {:left    (* cell-size x)
                           :width   cell-size
                           :height  cell-size}}
                  [:span]]))]))
        :div#age
          {:width default-width}
          [:span (str age)]])]))

(bind! "#world"
  [:section#world
   (let [{board :board, height :height, width :width, age :age} @world
         cell-size  default-size]
     [:svg#board
      {:viewBox (str "0 0 960 400")}
      (unify
        (reduce concat []
          (map-indexed
            (fn [y row]
              (filter identity
                (map-indexed
                  (fn [x alive?]
                    (when alive?
                      [x y]))
                  row)))
            board))
        (fn [[x y]]
          [:rect
           {:x    (* cell-size x)
            :y    (* cell-size y)
            :width   cell-size
            :height  cell-size
            :style {:fill  "#0f0"}}]))])])

(defn- evolve-world [world evolved]
  (xhr/send "/evolve"
    #(evolved
       (reader/read-string
         (.getResponseText (.-target %))))
    "POST"
    (pr-str world)
    (js-obj "Content-Type" "application/edn")))

(defn mk-world [width height]
  {:board   (vec (for [y (range height)]
                   (vec (for [x (range width)]
                          (= 0 (rand-int 2))))))
   :age     0
   :width   width
   :height  height})

(defn- live []
  (js/setTimeout
    (fn []
      (evolve-world @world
        #(do
           (reset! world %)
           (live))))
    default-interval))

(reset! world
  (mk-world default-width default-height))
(live)
