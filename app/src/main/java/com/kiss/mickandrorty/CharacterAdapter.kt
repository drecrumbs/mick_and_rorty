/*
 * Designed and developed by 2022 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kiss.mickandrorty

import android.os.SystemClock
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.kiss.mickandrorty.R
import com.kiss.mickandrorty.databinding.ItemCharacterBinding
import com.kiss.mickandrorty.model.character.Character
import com.skydoves.bindables.BindingListAdapter
import com.skydoves.bindables.binding
import com.skydoves.transformationlayout.TransformationCompat.startActivity

class CharacterAdapter : BindingListAdapter<Character, CharacterAdapter.CharacterViewHolder>(diffUtil) {

  private var onClickedAt = 0L

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder =
    parent.binding<ItemCharacterBinding>(R.layout.item_character).let(::CharacterViewHolder)

  override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) =
    holder.bindCharacter(getItem(position))

  inner class CharacterViewHolder constructor(
    private val binding: ItemCharacterBinding
  ) : RecyclerView.ViewHolder(binding.root) {

    init {
      binding.root.setOnClickListener {
        val position = bindingAdapterPosition.takeIf { it != NO_POSITION }
          ?: return@setOnClickListener
        val currentClickedAt = SystemClock.elapsedRealtime()
        if (currentClickedAt - onClickedAt > binding.transformationLayout.duration) {
          it.findNavController().navigate(
            FirstFragmentDirections.actionFirstFragmentToSecondFragment(getItem(position))
          )
         // MainActivity.startActivity(binding.transformationLayout, getItem(position))
          onClickedAt = currentClickedAt
        }
      }
    }

    fun bindCharacter(character: Character) {
      binding.character = character
      binding.executePendingBindings()
    }
  }

  companion object {
    private val diffUtil = object : DiffUtil.ItemCallback<Character>() {

      override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean =
        oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean =
        oldItem == newItem
    }
  }
}
