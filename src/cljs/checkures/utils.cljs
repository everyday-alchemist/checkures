(ns checkures.utils)

(def init-pos
  [[:black :black :black :black]
   [:black :black :black :black]
   [:black :black :black :black]
   [:none :none :none :none]
   [:none :none :none :none]
   [:red :red :red :red]
   [:red :red :red :red]
   [:red :red :red :red]])

(def edges
  #{#{[0 0] [0 1]}
    #{[0 0] [1 1]}
    #{[1 0] [1 1]}
    #{[1 0] [2 1]}
    #{[2 0] [2 1]}
    #{[2 0] [3 1]}
    #{[3 0] [3 1]}

    #{[0 1] [0 2]}
    #{[1 1] [0 2]}
    #{[1 1] [1 2]}
    #{[2 1] [1 2]}
    #{[2 1] [2 2]}
    #{[3 1] [2 2]}
    #{[3 1] [3 2]}

    #{[0 2] [0 3]}
    #{[0 2] [1 3]}
    #{[1 2] [1 3]}
    #{[1 2] [2 3]}
    #{[2 2] [2 3]}
    #{[2 2] [3 3]}
    #{[3 2] [3 3]}

    #{[0 3] [0 4]}
    #{[1 3] [0 4]}
    #{[1 3] [1 4]}
    #{[2 3] [1 4]}
    #{[2 3] [2 4]}
    #{[3 3] [2 4]}
    #{[3 3] [3 4]}

    #{[0 4] [0 5]}
    #{[0 4] [1 5]}
    #{[1 4] [1 5]}
    #{[1 4] [2 5]}
    #{[2 4] [2 5]}
    #{[2 4] [3 5]}
    #{[3 4] [3 5]}

    #{[0 5] [0 6]}
    #{[1 5] [0 6]}
    #{[1 5] [1 6]}
    #{[2 5] [1 6]}
    #{[2 5] [2 6]}
    #{[3 5] [2 6]}
    #{[3 5] [3 6]}

    #{[0 6] [0 7]}
    #{[0 6] [1 7]}
    #{[1 6] [1 7]}
    #{[1 6] [2 7]}
    #{[2 6] [2 7]}
    #{[2 6] [3 7]}
    #{[3 6] [3 7]}})

(defn get-2d
  [board row col]
  (get (get board row) col))

(defn conn?
  "returns true if from is connected to to"
  [adj from to]
  (contains? adj #{from to}))

;(defn occupied?
  ;[board row col]
  

(defn dir
  "returns the direction of a move from arg1 to arg2"
  [[_ y1] [_ y2]]
  (if (< y1 y2)
    :down
    :up))

(defn even-row
  "turn even rows into html"
  [row]
  (into [:tr]
        (interleave (repeat 4 [:td {:class :unplayable}])
                    (map #(vec [:td (assoc {} :class %)]) row))))

(defn odd-row
  "turn odd rows into html"
  [row]
  (into [:tr]
        (interleave (map #(vec [:td (assoc {} :class %)]) row)
                    (repeat 4 [:td {:class :unplayable}]))))

(defn show-board
  [board]
  (into [:table]
        (interleave
         (map #(even-row %) (take-nth 2 board))
         (map #(odd-row %) (take-nth 2 (rest board))))))
