(ns cgolws.core)

(defn live-neighbors [x0 y0 {width  :width,
                             height :height,
                             board  :board}]
  (count
    (filter
      (fn [[x y]]
        (and
          (>= x 0) (< x width)
          (>= y 0) (< y height)
          (-> board (nth y) (nth x))))
      [[(dec x0) (dec y0)]
       [     x0  (dec y0)]
       [(inc x0) (dec y0)]
       [(dec x0)      y0]
       [(inc x0)      y0]
       [(dec x0) (inc y0)]
       [     x0  (inc y0)]
       [(inc x0) (inc y0)]])))

(defn evolve-world [{board  :board,
                     height :height,
                     width  :width,
                     age    :age,
                     :as world}]
  (assoc world
    :age   (inc age)
    :board (vec (pmap
                  (fn [y]
                    (vec (map
                           (fn [x]
                             (let [alive?     (-> world :board (nth y) (nth x))
                                   neighbors  (live-neighbors x y world)]
                               (if alive?
                                 (or (= neighbors 2) (= neighbors 3))
                                 (= neighbors 3))))
                           (range width))))
                  (range height)))))

(defn validate-world [{board  :board,
                       width  :width,
                       height :height
                       :or {i 0}
                       :as world}]
  (let [height (or height (count board))]
    (if (and (> height 0)
          (= height (count board)))
      (let [width (or width (count (first board)))]
        (if (and (> width 0)
                (every? #(= (count %) width) board))
          (assoc world
            :board  board
            :width  width
            :height height)
          (assoc world
            :invalid {:width [width (map #(count %) board)]})))
      (assoc world
        :invalid {:height [height (count board)]}))))
