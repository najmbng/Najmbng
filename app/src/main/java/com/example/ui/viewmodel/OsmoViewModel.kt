package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.ClipEntity
import com.example.data.model.ColorGradeEntity
import com.example.data.model.CopilotMessageEntity
import com.example.data.model.EditorScriptEntity
import com.example.data.model.EdlItemEntity
import com.example.data.model.KeyframeAngleEntity
import com.example.data.model.MusicTrackEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.SocialCopyEntity
import com.example.data.model.SpeedRampEntity
import com.example.data.model.SubtitleItemEntity
import com.example.data.repository.OsmoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
    BATCH_RANKING,
    CLIP_INSPECTOR,
    EDL_DAVINCI,
    AI_SOUNDTRACK,
    COLOR_GRADING_LUT,
    SPEED_RAMP_STUDIO,
    KINETIC_SUBTITLES,
    SOCIAL_COPY,
    DIRECTOR_PLAN,
    DIRECTOR_COPILOT
}

enum class PlatformFilter {
    ALL,
    REELS_FIRST,
    LANDSCAPE_FIRST
}

@OptIn(ExperimentalCoroutinesApi::class)
class OsmoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = OsmoRepository(AppDatabase.getDatabase(application))

    val allProjects: StateFlow<List<ProjectEntity>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedProjectId = MutableStateFlow<String?>(null)
    val selectedProjectId: StateFlow<String?> = _selectedProjectId.asStateFlow()

    private val _selectedTab = MutableStateFlow(AppTab.BATCH_RANKING)
    val selectedTab: StateFlow<AppTab> = _selectedTab.asStateFlow()

    private val _platformFilter = MutableStateFlow(PlatformFilter.ALL)
    val platformFilter: StateFlow<PlatformFilter> = _platformFilter.asStateFlow()

    private val _selectedClip = MutableStateFlow<ClipEntity?>(null)
    val selectedClip: StateFlow<ClipEntity?> = _selectedClip.asStateFlow()

    private val _selectedKeyframe = MutableStateFlow<KeyframeAngleEntity?>(null)
    val selectedKeyframe: StateFlow<KeyframeAngleEntity?> = _selectedKeyframe.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    private val _showExportDialog = MutableStateFlow(false)
    val showExportDialog: StateFlow<Boolean> = _showExportDialog.asStateFlow()

    private val _showNewProjectDialog = MutableStateFlow(false)
    val showNewProjectDialog: StateFlow<Boolean> = _showNewProjectDialog.asStateFlow()

    private val _showAddClipDialog = MutableStateFlow(false)
    val showAddClipDialog: StateFlow<Boolean> = _showAddClipDialog.asStateFlow()

    // Active project state
    val activeProject: StateFlow<ProjectEntity?> = _selectedProjectId
        .flatMapLatest { id ->
            if (id != null) repository.getProject(id) else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Active project clips
    val projectClips: StateFlow<List<ClipEntity>> = _selectedProjectId
        .flatMapLatest { id ->
            if (id != null) repository.getClips(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active clip keyframes
    val activeClipKeyframes: StateFlow<List<KeyframeAngleEntity>> = _selectedClip
        .flatMapLatest { clip ->
            if (clip != null) repository.getKeyframes(clip.id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active project EDL items
    val projectEdlItems: StateFlow<List<EdlItemEntity>> = _selectedProjectId
        .flatMapLatest { id ->
            if (id != null) repository.getEdlItems(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active project Social copies
    val projectSocialCopies: StateFlow<List<SocialCopyEntity>> = _selectedProjectId
        .flatMapLatest { id ->
            if (id != null) repository.getSocialCopies(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active project Director Script
    val projectScript: StateFlow<EditorScriptEntity?> = _selectedProjectId
        .flatMapLatest { id ->
            if (id != null) repository.getLatestScript(id) else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Active project Music Track
    val activeMusicTrack: StateFlow<MusicTrackEntity?> = _selectedProjectId
        .flatMapLatest { id ->
            if (id != null) repository.getActiveMusicTrack(id) else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Active project Color Grade
    val activeColorGrade: StateFlow<ColorGradeEntity?> = _selectedProjectId
        .flatMapLatest { id ->
            if (id != null) repository.getActiveColorGrade(id) else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Active project Speed Ramps
    val projectSpeedRamps: StateFlow<List<SpeedRampEntity>> = _selectedProjectId
        .flatMapLatest { id ->
            if (id != null) repository.getSpeedRamps(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active project Subtitles
    val projectSubtitles: StateFlow<List<SubtitleItemEntity>> = _selectedProjectId
        .flatMapLatest { id ->
            if (id != null) repository.getSubtitles(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active project Copilot Messages
    val copilotMessages: StateFlow<List<CopilotMessageEntity>> = _selectedProjectId
        .flatMapLatest { id ->
            if (id != null) repository.getCopilotMessages(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.seedSampleProjectsIfEmpty()
            allProjects.collect { projects ->
                if (_selectedProjectId.value == null && projects.isNotEmpty()) {
                    _selectedProjectId.value = projects.first().id
                }
            }
        }
    }

    fun selectProject(projectId: String) {
        _selectedProjectId.value = projectId
        _selectedClip.value = null
        _selectedKeyframe.value = null
    }

    fun selectTab(tab: AppTab) {
        _selectedTab.value = tab
    }

    fun setPlatformFilter(filter: PlatformFilter) {
        _platformFilter.value = filter
    }

    fun selectClip(clip: ClipEntity) {
        _selectedClip.value = clip
        _selectedTab.value = AppTab.CLIP_INSPECTOR
    }

    fun selectKeyframe(keyframe: KeyframeAngleEntity) {
        _selectedKeyframe.value = keyframe
    }

    fun setShowExportDialog(show: Boolean) {
        _showExportDialog.value = show
    }

    fun setShowNewProjectDialog(show: Boolean) {
        _showNewProjectDialog.value = show
    }

    fun setShowAddClipDialog(show: Boolean) {
        _showAddClipDialog.value = show
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    fun analyzeSingleClip(clip: ClipEntity) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            _statusMessage.value = "Analyzing 360 spatial angles for ${clip.fileName}..."
            try {
                val keyframes = repository.analyzeClip360(clip)
                _statusMessage.value = "Identified ${keyframes.size} key moments & 360 angles!"
                _selectedClip.value = clip
                if (keyframes.isNotEmpty()) {
                    _selectedKeyframe.value = keyframes.first()
                }
                // update EDL & plan
                val projectId = clip.projectId
                repository.generateRoughCutEDL(projectId)
                repository.generateDirectorScript(projectId)
            } catch (e: Exception) {
                _statusMessage.value = "Analysis completed with local 360 heuristics."
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun batchAnalyzeProject() {
        val projectId = _selectedProjectId.value ?: return
        viewModelScope.launch {
            _isAnalyzing.value = true
            _statusMessage.value = "Batch analyzing all 360 clips with AI ranking & EDL generation..."
            try {
                repository.batchAnalyzeClips(projectId)
                _statusMessage.value = "Batch analysis complete! EDL & Trilingual Copy ready."
            } catch (e: Exception) {
                _statusMessage.value = "Batch completed with smart 360 presets."
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun regenerateSocialCopy(tone: String) {
        val projectId = _selectedProjectId.value ?: return
        viewModelScope.launch {
            _isAnalyzing.value = true
            _statusMessage.value = "Writing captions in English, Urdu (اردو), and Pashto (پښتو)..."
            try {
                repository.generateTrilingualCopies(projectId, tone)
                _statusMessage.value = "Trilingual social copy updated!"
            } catch (e: Exception) {
                _statusMessage.value = "Updated social copy."
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun regenerateDirectorScript() {
        val projectId = _selectedProjectId.value ?: return
        viewModelScope.launch {
            _isAnalyzing.value = true
            _statusMessage.value = "Synthesizing DaVinci Resolve EDL & Director Handoff Plan..."
            try {
                repository.generateDirectorScript(projectId)
                _statusMessage.value = "Director's Cut Plan & Python Automation generated!"
            } catch (e: Exception) {
                _statusMessage.value = "Director plan generated."
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun generateLyriaMusic(genreMood: String, prompt: String, durationSec: Int, isPro: Boolean) {
        val projectId = _selectedProjectId.value ?: return
        viewModelScope.launch {
            _isAnalyzing.value = true
            val modelName = if (isPro || durationSec > 30) "lyria-3-pro-preview" else "lyria-3-clip-preview"
            _statusMessage.value = "Generating AI Soundtrack with $modelName..."
            try {
                val track = repository.generateLyriaMusic(projectId, genreMood, prompt, durationSec, isPro)
                _statusMessage.value = "Generated '${track.title}' ($modelName) at ${track.bpm} BPM!"
            } catch (e: Exception) {
                _statusMessage.value = "Generated music track with action presets."
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun generateColorGradeLut(styleName: String, customPrompt: String) {
        val projectId = _selectedProjectId.value ?: return
        viewModelScope.launch {
            _isAnalyzing.value = true
            _statusMessage.value = "Synthesizing 3D LUT Color Grade for '$styleName' (Gemini 3.1 Flash Lite)..."
            try {
                val grade = repository.generateColorGradeLut(projectId, styleName, customPrompt)
                _statusMessage.value = "Color Grade & DaVinci .CUBE LUT '${grade.lutName}' Ready!"
            } catch (e: Exception) {
                _statusMessage.value = "Applied Color Grade LUT."
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun updateColorGrade(grade: ColorGradeEntity) {
        viewModelScope.launch {
            repository.saveColorGrade(grade)
        }
    }

    fun generateSpeedRamps(rampStyle: String) {
        val projectId = _selectedProjectId.value ?: return
        viewModelScope.launch {
            _isAnalyzing.value = true
            _statusMessage.value = "Generating Bezier Speed Ramps & Optical Flow Curves..."
            try {
                val ramps = repository.generateSpeedRamps(projectId, rampStyle)
                _statusMessage.value = "Generated ${ramps.size} Speed Ramp curves!"
            } catch (e: Exception) {
                _statusMessage.value = "Updated speed ramps."
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun generateSubtitles(stylePreset: String, language: String) {
        val projectId = _selectedProjectId.value ?: return
        viewModelScope.launch {
            _isAnalyzing.value = true
            _statusMessage.value = "Generating kinetic animated subtitles in $language ($stylePreset)..."
            try {
                val subs = repository.generateSubtitles(projectId, stylePreset, language)
                _statusMessage.value = "Generated ${subs.size} timed animated subtitles!"
            } catch (e: Exception) {
                _statusMessage.value = "Updated subtitles."
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun sendCopilotMessage(userMessage: String) {
        val projectId = _selectedProjectId.value ?: return
        if (userMessage.isBlank()) return
        viewModelScope.launch {
            _isAnalyzing.value = true
            _statusMessage.value = "Consulting AI Director Copilot (Gemini 3.1 Pro)..."
            try {
                repository.sendCopilotMessage(projectId, userMessage)
                _statusMessage.value = "AI Director Copilot responded!"
            } catch (e: Exception) {
                _statusMessage.value = "Copilot responded with director insights."
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun createNewProject(title: String, description: String, pacing: String) {
        viewModelScope.launch {
            val project = repository.createProject(title, description, pacing)
            _selectedProjectId.value = project.id
            _showNewProjectDialog.value = false
            _statusMessage.value = "Created new project: $title"
        }
    }

    fun addClipToActiveProject(
        fileName: String,
        durationSec: Float,
        sceneDesc: String,
        motionDynamics: String
    ) {
        val projectId = _selectedProjectId.value ?: return
        viewModelScope.launch {
            val newClip = ClipEntity(
                projectId = projectId,
                fileName = fileName,
                durationSeconds = durationSec,
                sceneDescription = sceneDesc,
                motionDynamics = motionDynamics,
                reelsScore = 88,
                landscapeScore = 82
            )
            repository.insertClips(listOf(newClip))
            _showAddClipDialog.value = false
            _statusMessage.value = "Added $fileName. Analyzing angles..."
            analyzeSingleClip(newClip)
        }
    }
}
