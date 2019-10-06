package kw.app.codebase.view.utility

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kw.app.codebase.vm.App

open class BaseFragment : Fragment() {
    protected val avm: App by activityViewModels()
}