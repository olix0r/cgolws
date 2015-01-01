# cgolws

Conway's Game of Life as a Service

A small client-server app in Clojure & ClojureScript that runs
conway's game of life simulation.

The server exposes a single API endpoint, `/evolve`, that accepts POST
requests that takes an
[EDN-formatted](https://github.com/edn-format/edn) world
representation like:

    {:width  3
     :height 3
     :age    0
     :board [[true  false false]
             [false true  false]
             [false false true]]}

And responds with an evolved board state:

    {:width  3
     :height 3
     :age    1
     :board [[false false false]
             [false true  false]
             [false false false]]}

The server parallelizes processing of this value, so it should provide
an advantage over client-side processing for large datasets. The
server performs some sanity-checking of the posted data and will
return a 400 if it cannot be validated. If data is posted
without a `:board`, however, a randomized board state will be
generated.

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

Then visit http://localhost:3000/

## License

Copyright © 2014 Oliver Gould
