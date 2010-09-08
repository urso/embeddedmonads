
package embeddedmonads

import scala.util.continuations._
import scalaz._
import scalaz.Monad

class EmbeddedMonad[M[_]]()(implicit monad:Monad[M]) {

    def runMonad[C](ctx: => Any @ cpsParam[M[Any], M[Any]]) : M[C] = {
        val tmp : M[Any] = reset {
            val x : Any = ctx
            monad.pure(x)
        }
        tmp.asInstanceOf[M[C]]
    }

    def get[A](value:M[A]) = shift { k:(A=>M[Any]) =>
        monad.bind(value, k)
    }

    class CPSMonadValue[A](m:M[A]) {
        def value = get[A](m)
    }

    implicit def monad2cpsmonadvalue[A](x:M[A]) : CPSMonadValue[A] =
            new CPSMonadValue(x)
}

object EmbeddedOption extends EmbeddedMonad[Option] {
    def runOption[C](ctx: => Any @cpsParam[Option[Any], Option[Any]]) : Option[C] =
        runMonad(ctx)
}

object EmbeddedSeq extends EmbeddedMonad[Seq] {
    def runSeq[C](ctx: => Any @cpsParam[Seq[Any], Seq[Any]]) : Seq[C] =
        runMonad(ctx)

    def guard[A](b:Boolean) = shift { k:(Unit => Seq[Any]) =>
        if (b) k() else Seq()
    }
}

object EmbeddedIterable extends EmbeddedMonad[Iterable] {
    def runIterable[C](ctx: => Any @cpsParam[Iterable[Any], Iterable[Any]]) : Iterable[C] =
        runMonad(ctx)

    def guard[A](b:Boolean) = shift { k:(Unit => Seq[Any]) =>
        if (b) k() else Seq()
    }
}

class EmbeddedState[S] extends EmbeddedMonad[PartialApply1Of2[State,S]#Apply] {
}

