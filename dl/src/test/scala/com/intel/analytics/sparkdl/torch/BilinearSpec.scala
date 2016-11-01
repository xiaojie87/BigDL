/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intel.analytics.sparkdl.torch



import com.intel.analytics.sparkdl.nn.Bilinear
import com.intel.analytics.sparkdl.tensor.Tensor
import com.intel.analytics.sparkdl.utils.RandomGenerator._
import com.intel.analytics.sparkdl.utils.Table
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
<<<<<<< HEAD
<<<<<<< HEAD

import scala.collection.mutable.HashMap
import scala.util.Random
=======
import scala.collection.mutable.HashMap
>>>>>>> add Bilinear layer and convert java.map to scala.map
=======

import scala.collection.mutable.HashMap
import scala.util.Random
>>>>>>> some modify of Bilinear

class BilinearSpec extends FlatSpec with BeforeAndAfter with Matchers{
  before {
    if (!TH.hasTorch()) {
      cancel("Torch is not installed")
    }
  }

  "A Bilinear " should "generate correct output and grad" in {
    val seed = 100
    RNG.setSeed(seed)

<<<<<<< HEAD
<<<<<<< HEAD
    val input1 = Tensor[Double](5, 5).apply1(e => Random.nextDouble())
=======
    val input1 = Tensor[Double](5, 5)
    input1(Array(1, 1)) = 1
    input1(Array(1, 2)) = 2
    input1(Array(1, 3)) = 3
    input1(Array(1, 4)) = 4
    input1(Array(1, 5)) = 5
    input1(Array(2, 1)) = 11
    input1(Array(2, 2)) = 22
    input1(Array(2, 3)) = 33
    input1(Array(2, 4)) = 44
    input1(Array(2, 5)) = 55
    input1(Array(3, 1)) = 10
    input1(Array(3, 2)) = 20
    input1(Array(3, 3)) = 30
    input1(Array(3, 4)) = 40
    input1(Array(3, 5)) = 50
    input1(Array(4, 1)) = 14
    input1(Array(4, 2)) = 24
    input1(Array(4, 3)) = 34
    input1(Array(4, 4)) = 44
    input1(Array(4, 5)) = 54
    input1(Array(5, 1)) = 9
    input1(Array(5, 2)) = 4
    input1(Array(5, 3)) = 13
    input1(Array(5, 4)) = 29
    input1(Array(5, 5)) = 32

>>>>>>> add Bilinear layer and convert java.map to scala.map
=======
    val input1 = Tensor[Double](5, 5).apply1(e => Random.nextDouble())
>>>>>>> some modify of Bilinear
    val input2 = Tensor[Double](5, 3).fill(2.toDouble)
    val gradOutput = Tensor[Double](5, 2).fill(1.toDouble)

    var input = new Table()
    input(1.toDouble) = input1
    input(2.toDouble) = input2

    val code = "torch.manualSeed(" + seed + ")\n" +
      "module = nn.Bilinear(5,3,2)\n" +
      "module:reset()\n" +
      "bias = module.bias\n" +
      "weight = module.weight\n" +
      "output = module:forward(input)\n" +
      "gradInput = module:backward(input,gradOutput)\n" +
      "gradBias = module.gradBias\n" +
      "gradWeight = module.gradWeight\n"

    val (luaTime, torchResult) = TH.run(code, Map("input" -> input, "gradOutput" -> gradOutput),
      Array("output", "gradInput", "bias", "weight", "grad", "gradBias", "gradWeight"))

    val luaOutput1 = torchResult("output").asInstanceOf[Tensor[Double]]
    val luaOutput2 = torchResult("gradInput").asInstanceOf[HashMap[Double, Tensor[Double]]]
    val luaBias = torchResult("bias").asInstanceOf[Tensor[Double]]
    val luaWeight = torchResult("weight").asInstanceOf[Tensor[Double]]
    val luaGradBias = torchResult("gradBias").asInstanceOf[Tensor[Double]]
    val luaGradWeight = torchResult("gradWeight").asInstanceOf[Tensor[Double]]

<<<<<<< HEAD
<<<<<<< HEAD
    val module = new Bilinear[Double](5, 3, 2)
=======
    val module = new Bilinear[Table, Tensor[Double], Double](5, 3, 2)
>>>>>>> add Bilinear layer and convert java.map to scala.map
=======
    val module = new Bilinear[Double](5, 3, 2)
>>>>>>> some modify of Bilinear
    val start = System.nanoTime()
    module.reset()
    val bias = module.bias
    val output = module.forward(input)
    val weight = module.weight
    val gradBias = module.gradBias
    val gradWeight = module.gradWeight
    val gradInput = module.backward(input, gradOutput)
    val end = System.nanoTime()
    val scalaTime = end - start

    output should be(luaOutput1)
    bias should be(luaBias)
    weight should be(luaWeight)
    gradBias should be(luaGradBias)
    gradWeight should be(luaGradWeight)

    val luagradInput1 = luaOutput2.get(1.0).getOrElse(null)
    val luagradInput2 = luaOutput2.get(2.0).getOrElse(null)

    val gradInput1 = gradInput.apply(1.toDouble).asInstanceOf[Tensor[Double]]
    val gradInput2 = gradInput.apply(2.toDouble).asInstanceOf[Tensor[Double]]
    gradInput1 should be(luagradInput1)
    gradInput2 should be(luagradInput2)

    println("Test case : Bilinear, Torch : " + luaTime +
      " s, Scala : " + scalaTime / 1e9 + " s")
  }
}
