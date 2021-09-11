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

(defn occupied?
  [board row col]
  (not= (get-2d board row col) :none))

(defn get-dir
  ; TODO refactor to only take col?
  "returns the direction of a move from arg1 to arg2"
  [[_ y1] [_ y2]]
  (if (< y1 y2)
    :down
    :up))

(defn get-valid-dirs
  [piece]
  (case piece
    :red #{:up}
    :black #{:down}
    #{:up :down}))


(defn valid-move?
  [board [from-row from-col] [to-row to-col]]
  (let [from-piece (get-2d board from-row from-col)
        to-piece (get-2d board to-row to-col)
        dir (get-dir [from-row from-col] [to-row to-col])
        valid-dirs (get-valid-dirs from-piece)
        dist (- from-col to-col)]
    (and 
      (not= from-piece :none)
      (= to-piece :none)
      (conn? board [from-row from-col] [to-row to-col])
      (contains? valid-dirs dir)
      (or (= dist 1) (= dist -1)))))

(defn even-row
  "turn even rows into html"
  [i row]
  (let [index (* i 2)]
  (into [:tr {:row index}]
        (interleave (repeat 4 [:td {:class :unplayable}])
                    (map-indexed #(vec [:td (assoc {} :class %2 :row index :col %1)]) row)))))
(defn odd-row
  "turn odd rows into html"
  [i row]
  (let [index (inc (* i 2))]
  (into [:tr {:row index}]
        (interleave (map-indexed #(vec [:td (assoc {} :class %2 :row index :col %1)]) row)
                    (repeat 4 [:td {:class :unplayable}])))))

(defn show-board
  [board]
  (into [:table]
        (interleave
         (map-indexed even-row (take-nth 2 board))
         (map-indexed odd-row (take-nth 2 (rest board))))))

;; TODO: fix the col/row x/y situation
