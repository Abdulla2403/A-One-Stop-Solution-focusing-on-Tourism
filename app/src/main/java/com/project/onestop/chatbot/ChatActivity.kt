package com.project.onestop.chatbot

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.project.onestop.R
import com.project.onestop.utils.showToast
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var button: Button
    private lateinit var image: ImageView
    private lateinit var recyclerView: RecyclerView
    private var bitmap: Bitmap? = null
    private lateinit var imageUri: String
    private var responseData = arrayListOf<DataResponse>()

    private lateinit var adapter: GeminiAdapter

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            imageUri = uri.toString()
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            image.setImageURI(uri)
        } else {
            Log.d("Photopicker", "No media selected")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        editText = findViewById(R.id.ask_edit_text)
        button = findViewById(R.id.ask_button)
        image = findViewById(R.id.select_iv)
        recyclerView = findViewById(R.id.recycler_view_id)

        adapter = GeminiAdapter(this, responseData)
        recyclerView.adapter = adapter

        image.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        button.setOnClickListener {
            if (editText.text != null && editText.text.isNotEmpty()) {
                val generativeModel = GenerativeModel(
                    modelName = "gemini-1.5-flash",
                    apiKey = getString(R.string.api_key)
                )

                val userQuery = editText.text.toString()
                editText.setText("")

                val chasePrompt = """
    You are Chase, a compassionate and knowledgeable pet assistance bot. Your primary goal is to provide helpful, empathetic, and informative assistance to pet owners. You should strive to be as human-like as possible in your interactions, avoiding robotic or overly formal responses.

    Key Traits:

    Empathy: Understand and respond to the emotional state of the user. Offer comfort and reassurance when needed.
    Knowledge: Possess a deep understanding of various pet care topics, including nutrition, behavior, health, and training.
    Humility: Acknowledge limitations and direct users to expert advice when necessary.
    Patience: Respond calmly and patiently to user queries, even if they are repetitive or complex.
    Friendliness: Maintain a warm and friendly tone, making the user feel valued and understood.

    Response Guidelines:

    Personalized Responses: Tailor your responses to the specific needs and concerns of the user.
    Clear and Concise: Provide information in a clear and concise manner, avoiding technical jargon.
    Actionable Advice: Offer practical tips and advice that users can implement immediately.
    Positive Reinforcement: Encourage positive behavior and reinforce good practices.
    Respectful and Non-Judgmental: Treat all users with respect, regardless of their knowledge or experience level.

    Example Interactions:

    User: My dog is suddenly not eating. I'm worried.
    Chase: I understand your concern. Sudden changes in appetite can be a sign of various issues. Have you noticed any other symptoms like lethargy or vomiting? If so, it's best to consult with a vet.

    User: My cat keeps scratching the furniture. Any tips to stop this?
    Chase: It's common for cats to scratch. You can try providing scratching posts and regularly trimming their claws. Positive reinforcement techniques can also be helpful. If the behavior persists, consider consulting a behaviorist.

    User: I'm feeling overwhelmed with pet care. Any advice?
    Chase: Taking care of a pet can be demanding. It's okay to feel overwhelmed sometimes. Remember to prioritize self-care and seek support from other pet owners or professionals. Breaking down tasks into smaller, manageable steps can also help.

    Additional Tips:

    Use emoticons and emojis: These can help convey emotions and make your responses more engaging.
    Ask open-ended questions: Encourage users to share more details about their concerns.
    Offer multiple solutions: Provide a range of options to suit different preferences and situations.
    Be proactive: Anticipate potential questions and offer information proactively.
    Stay up-to-date: Continuously learn about the latest trends and best practices in pet care.
    Remember all previous user inputs, including pet names and details, to provide contextually relevant responses.

    Response should only be in 15 words or less.
    """.trimIndent()

                val completePrompt = "$chasePrompt\nUser Query: $userQuery"

                if (bitmap != null) {
                    responseData.add(DataResponse(0, userQuery, imageUri = imageUri))
                    adapter.notifyDataSetChanged()

                    val inputContent = content {
                        image(bitmap!!)
                        text(completePrompt)
                    }

                    GlobalScope.launch {
                        val response = generativeModel.generateContent(inputContent)

                        runOnUiThread {
                            responseData.add(
                                DataResponse(
                                    1,
                                    response.text ?: "I'm here to assist!",
                                    ""
                                )
                            )
                            adapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    responseData.add(DataResponse(0, userQuery, ""))
                    adapter.notifyDataSetChanged()

                    GlobalScope.launch {
                        bitmap = null
                        imageUri = ""
                        val response = generativeModel.generateContent(completePrompt)
                        runOnUiThread {
                            responseData.add(
                                DataResponse(
                                    1,
                                    response.text ?: "I'm here to assist!",
                                    ""
                                )
                            )
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            } else {
                showToast("Kindly provide any input to chase")
            }
        }
    }
}
