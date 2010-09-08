
embeddedmonads
==============

Support for implicitly mixing monads (for example Option or List-Monad) with
any scala code as alternative to for-comprehension.

## compile

this projects was created with [sbt 0.7.4](http://code.google.com/p/simple-build-tool/).

Compile and create jar-Archive in "target"-directory with:

    sbt package

Install locally, so can be found by other sbt-projects:

    sbt publish-local


## Examples

Simple Example for using the Option-Monad:

    scala> import embeddedmonads.EmbeddedOption._
    import embeddedmonads.EmbeddedOption._

    scala> runOption[Int] {
         |   if (Some(true).value) {
         |     Some(1).value
         |   } else {
         |     Some(2).value + Some(3).value
         |   }
         | }
    res0: Option[Int] = Some(1)

    scala> val x:Option[Int] = None
    scala> runOption[Int] { x.value + 10 * Some(2).value }
    res1: Option[Int] = None
     
    scala> val x:Option[Int] = Some(5)                    
    scala> runOption[Int] { x.value + 10 * Some(2).value }
    res4: Option[Int] = Some(25)

List-Monad example:

scala> import embeddedmonads.EmbeddedSeq._
import embeddedmonads.EmbeddedSeq._

scala> runSeq[(Int,Char)] { (Seq(1,2,3).value, Seq('a', 'b', 'c').value) }
res5: Seq[(Int, Char)] = List((1,a), (1,b), (1,c), (2,a), (2,b), (2,c), (3,a),
(3,b), (3,c))


