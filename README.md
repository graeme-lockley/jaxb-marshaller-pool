# jaxb-marshaller-pool

I wrote a note which investigated the different ways in which XML can be created in Java.  Part of this note was to look at the different timings of each of the methods to understand better what the relative trade-offs are.  What came out of this piece of work is that the setup cost of creating a JAXB marshaller every time an object needs to be marshalled can introduce a 500x overhead pver 10,000 iterations when comparsed to reusing marshallers.

Unfortunately marshallers are not thread-safe.

As an exercise I created a simple JAXB marshaller pool that can be dropped into any multi-threaded application.
