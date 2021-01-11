package es.uniovi.sdm.quarantinementalhealthtracker.ui.days.survey

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.quickbirdstudios.surveykit.AnswerFormat
import com.quickbirdstudios.surveykit.FinishReason
import com.quickbirdstudios.surveykit.NavigableOrderedTask
import com.quickbirdstudios.surveykit.SurveyTheme
import com.quickbirdstudios.surveykit.result.TaskResult
import com.quickbirdstudios.surveykit.result.question_results.ScaleQuestionResult
import com.quickbirdstudios.surveykit.steps.InstructionStep
import com.quickbirdstudios.surveykit.steps.QuestionStep
import com.quickbirdstudios.surveykit.survey.SurveyView
import es.uniovi.sdm.quarantinementalhealthtracker.R
import es.uniovi.sdm.quarantinementalhealthtracker.databinding.FragmentSurveyBinding


class SurveyFragment : Fragment() {
    private lateinit var survey: SurveyView
    private var _binding: FragmentSurveyBinding? = null
    private val binding get() = _binding!!
    private val args: SurveyFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSurveyBinding.inflate(inflater, container, false)
        val root = binding.root
        survey = binding.surveyView
        val steps = listOf(
            InstructionStep(
                text = resources.getString(R.string.survey_title),
                buttonText = resources.getString(R.string.survey_start)
            )
        )
        val questions = (1..9).map { x -> getQuestionStep(x) }
        val all = steps + questions
        val task = NavigableOrderedTask(steps = all)
        survey.onSurveyFinish = { taskResult: TaskResult, reason: FinishReason ->
            if (reason == FinishReason.Completed) {
                val result = taskResult.results.flatMap { stepResult ->
                    stepResult.results.filterIsInstance<ScaleQuestionResult>().mapNotNull { questionResult ->
                        questionResult.answer?.toInt()
                    }
                }
                completeSurvey(result)
            }
        }
        val configuration = SurveyTheme(
            themeColorDark = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark),
            themeColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            textColor = ContextCompat.getColor(requireContext(), R.color.cyan_text)
        )
        survey.start(task, configuration)
        return root
    }

    private fun getQuestionBody(n: Int) = resources.getString(
        resources.getIdentifier(
            "survey_$n",
            "string",
            requireContext().packageName
        )
    )

    private fun getQuestionStep(n: Int) = QuestionStep(
        title = "Step $n",
        text = getQuestionBody(n),
        answerFormat = AnswerFormat.ScaleAnswerFormat(
            minimumValue = 0,
            maximumValue = 3,
            step = 1f,
            minimumValueDescription = "",
            maximumValueDescription = ""
        )
    )

    private fun completeSurvey(result: List<Int>) {
        val directions =
            SurveyFragmentDirections.actionSurveyFragmentToDayGraph(args.dayId, result.toIntArray())
        findNavController().navigate(directions)
    }
}