import React, { useState, useEffect } from 'react';
import { Heart, Calendar, TestTube, CheckSquare, BookOpen, Baby, Lock, Unlock, Download, Bell, Plus, Trash2, Edit3, Menu, X } from 'lucide-react';
import { fetchNotesFromKV, saveNotesToKV } from './kvService';

export default function App() {
  const [currentTab, setCurrentTab] = useState('home');
  const [isGoldTheme, setIsGoldTheme] = useState(false);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  // Checklist items state
  const [checklist, setChecklist] = useState([
    { id: 'chk-defrost', symbol: '🧊', title: 'Plan de décongélation terminé', dateText: '13 octobre · soumis', badgeText: '✓ Validé', badgeType: 'past', isCompleted: true, description: 'Formulaire indiquant une préférence pour un garçon (« Male »). Sous réserve des résultats PGT-A.' },
    { id: 'chk-nortrel', symbol: '💊', title: 'Maintien de la médication quotidienne', dateText: 'Chaque jour', badgeText: '⏱️ En cours', badgeType: 'today', isCompleted: false, description: 'Continuer le Nortrel selon les instructions actuelles de l’équipe RMA.' },
    { id: 'chk-sis', symbol: '🏥', title: 'Hystérosonographie SIS', dateText: '28 octobre · 08 h 00', badgeText: '⏳ AUJOURD\'HUI', badgeType: 'today', isCompleted: false, description: 'RMA East Side. Examen pour évaluer la cavité utérine et l\'endomètre.' },
    { id: 'chk-hystero', symbol: '🔬', title: 'Hystéroscopie opératoire', dateText: '30 octobre · 07 h 30', badgeText: '⏳ J-2', badgeType: 'imminent', isCompleted: false, description: 'Gramercy Surgery Center. Intervention pour préparer une cavité utérine optimale avant le FET.' },
    { id: 'chk-pgta', symbol: '🧬', title: 'Surveiller les résultats PGT-A', dateText: '23–28 octobre', badgeText: '⏳ En cours', badgeType: 'today', isCompleted: false, description: 'Analyse chromosomique des 8 blastocystes vitrifiés.' }
  ]);

  // Journal notes state with Vercel KV persistence
  const [notes, setNotes] = useState([
    { id: '1', title: 'En attente douce des résultats PGT-A', date: '2026-10-26', category: 'Émotion', content: 'Chaque jour nous rapproche. Rester sereine, respirer profondément, et faire confiance au laboratoire.' },
    { id: '2', title: 'Après la ponction : 42 ovocytes !', date: '2026-10-01', category: 'Jalon', content: '42 ovocytes prélevés, 34 matures et 8 magnifiques blastocystes vitrifiés. Très fière ♡' }
  ]);
  const [newNoteTitle, setNewNoteTitle] = useState('');
  const [newNoteContent, setNewNoteContent] = useState('');

  // Fetch notes from Vercel KV on mount
  useEffect(() => {
    fetchNotesFromKV().then((fetched) => {
      if (fetched && Array.isArray(fetched) && fetched.length > 0) {
        setNotes(fetched);
      }
    });
  }, []);

  const toggleCheck = (id) => {
    const updated = checklist.map(item => item.id === id ? { ...item, isCompleted: !item.isCompleted } : item);
    setChecklist(updated);
  };

  const addNote = (e) => {
    e.preventDefault();
    if (!newNoteTitle) return;
    const updatedNotes = [
      { id: Date.now().toString(), title: newNoteTitle, date: new Date().toISOString().split('T')[0], category: 'Personnel', content: newNoteContent },
      ...notes
    ];
    setNotes(updatedNotes);
    saveNotesToKV(updatedNotes);
    setNewNoteTitle('');
    setNewNoteContent('');
  };

  const deleteNote = (id) => {
    const updatedNotes = notes.filter(n => n.id !== id);
    setNotes(updatedNotes);
    saveNotesToKV(updatedNotes);
  };

  const headerBg = isGoldTheme ? 'bg-[#947230]' : 'bg-[#5C3B24]';

  const navItems = [
    { id: 'home', label: 'Accueil', icon: '⌂' },
    { id: 'calendar', label: 'Agenda', icon: '▦' },
    { id: 'lab', label: 'Labo', icon: '🧪' },
    { id: 'checklist', label: 'Actions', icon: '☑' },
    { id: 'journal', label: 'Notes', icon: '✍' },
    { id: 'obstetric', label: 'Bébé', icon: '👶' },
  ];

  return (
    <div className="min-h-screen bg-[#FAF6F0] text-[#2B2320] flex flex-col md:flex-row overflow-x-hidden">
      
      {/* Desktop Sidebar Navigation */}
      <aside className={`hidden md:flex flex-col w-64 ${headerBg} text-white p-6 shadow-xl transition-colors duration-300 justify-between shrink-0`}>
        <div className="space-y-6">
          <div className="flex items-center gap-3">
            <span className="text-2xl text-[#FFDF7D]">♡</span>
            <div>
              <h1 className="font-serif font-bold text-lg leading-tight">Mon Parcours</h1>
              <p className="text-xs text-[#F0DEC9]">Fatou ♥ · RMA East Side</p>
            </div>
          </div>
          <nav className="space-y-1.5">
            {navItems.map(item => (
              <button
                key={item.id}
                onClick={() => setCurrentTab(item.id)}
                className={`w-full flex items-center gap-3 px-4 py-3 min-h-[48px] rounded-xl text-sm font-semibold transition ${currentTab === item.id ? 'bg-white/20 text-white shadow-inner' : 'text-white/80 hover:bg-white/10 hover:text-white'}`}
              >
                <span className="text-lg">{item.icon}</span>
                <span>{item.label}</span>
              </button>
            ))}
          </nav>
        </div>
        <div className="space-y-3 pt-6 border-t border-white/20">
          <button 
            onClick={() => setIsGoldTheme(!isGoldTheme)}
            className="w-full py-3 min-h-[48px] text-xs font-bold bg-black/20 hover:bg-black/30 rounded-xl transition flex items-center justify-center gap-2"
          >
            {isGoldTheme ? '✨ Thème Or Actif' : '🤎 Thème Fatou Actif'}
          </button>
          <button 
            onClick={() => alert("Résumé PDF généré et téléchargé avec succès !")}
            className="w-full py-3 min-h-[48px] text-xs font-bold bg-white text-[#5C3B24] hover:bg-[#F4EBD8] rounded-xl transition flex items-center justify-center gap-2 shadow"
          >
            <Download size={14} /> Télécharger Résumé PDF
          </button>
        </div>
      </aside>

      {/* Main Container */}
      <div className="flex-1 flex flex-col min-h-screen w-full overflow-x-hidden">
        
        {/* Mobile Header */}
        <div className={`md:hidden ${headerBg} text-white p-4 flex items-center justify-between shadow-md transition-colors duration-300 sticky top-0 z-20`}>
          <div className="flex items-center gap-2.5">
            <span className="text-xl text-[#FFDF7D]">♡</span>
            <div>
              <h1 className="text-base font-serif font-bold leading-tight">Mon Parcours</h1>
              <p className="text-[10px] text-[#F0DEC9]">Fatou ♥ · RMA</p>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <button 
              onClick={() => setIsGoldTheme(!isGoldTheme)}
              className="px-3 py-2 min-h-[44px] text-[11px] font-extrabold bg-black/20 rounded-full border border-white/20 flex items-center justify-center"
            >
              {isGoldTheme ? '✨ Or' : '🤎 Fatou'}
            </button>
            <button 
              onClick={() => alert("Résumé PDF généré !")}
              className="px-3 py-2 min-h-[44px] text-[11px] font-bold bg-black/30 rounded-full border border-white/20 flex items-center gap-1.5 justify-center"
            >
              <Download size={12} /> PDF
            </button>
          </div>
        </div>

        {/* Content Area */}
        <main className="flex-1 max-w-4xl w-full mx-auto p-4 sm:p-6 lg:p-8 space-y-6 pb-28 md:pb-8">
          
          {/* TAB 1: HOME */}
          {currentTab === 'home' && (
            <div className="space-y-6">
              {/* Hero Banner */}
              <div className="bg-gradient-to-br from-[#F4EBD8] via-[#FCF9F5] to-[#EAE0D0] border border-[#D8C7B3] rounded-2xl p-6 sm:p-8 shadow-sm">
                <span className="text-xs font-extrabold text-[#5C3B24] tracking-wider uppercase">VOTRE PARCOURS FIV</span>
                <h2 className="text-2xl sm:text-3xl lg:text-4xl font-serif font-medium text-[#2B2320] mt-1.5 leading-snug">De la première injection au premier battement de cœur</h2>
                <p className="text-sm sm:text-base text-[#6E6058] mt-2.5">Chaque étape chez RMA of New York est documentée avec précision et suivie en toute sécurité.</p>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {/* Graduation Countdown Card */}
                <div className="bg-[#FBF6EE] border border-[#5C3B24] rounded-2xl p-5 sm:p-6 shadow-sm space-y-4">
                  <div className="flex justify-between items-center">
                    <div className="flex items-center gap-3">
                      <div className="w-11 h-11 rounded-full bg-[#F4EBD8] flex items-center justify-center text-xl shrink-0">🎓</div>
                      <div>
                        <h3 className="font-serif font-bold text-base text-[#5C3B24]">Compte à rebours de Graduation</h3>
                        <p className="text-xs text-[#6E6058]">Transition RMA vers OBGYN (Semaines 8–10)</p>
                      </div>
                    </div>
                    <span className="text-xs font-bold bg-[#EAF4ED] text-[#2B7853] px-3 py-1.5 rounded-full border border-[#C7E2D0] shrink-0">Semaine 9</span>
                  </div>
                  <div className="bg-white rounded-xl p-4 border border-[#E5D4C3] flex justify-between items-center">
                    <div>
                      <p className="text-xs text-[#6E6058]">Âge gestationnel estimé :</p>
                      <p className="text-base font-serif font-bold text-[#2B2320]">9 semaines et 2 jours</p>
                    </div>
                    <div className="bg-[#FAF6F0] border border-[#E2D3C4] px-4 py-2.5 rounded-xl text-center">
                      <span className="text-2xl font-extrabold text-[#5C3B24]">14</span>
                      <p className="text-[9px] font-bold text-[#6E6058]">JOURS CIBLE</p>
                    </div>
                  </div>
                </div>

                {/* Continued Support Meds */}
                <div className="bg-white border border-[#E2D3C4] rounded-2xl p-5 sm:p-6 shadow-sm space-y-4">
                  <div className="flex justify-between items-center">
                    <div className="flex items-center gap-3">
                      <div className="w-11 h-11 rounded-full bg-[#EAF4ED] flex items-center justify-center text-xl shrink-0">💊</div>
                      <div>
                        <h3 className="font-serif font-bold text-base text-[#2B2320]">Soutien Hormonal Continu</h3>
                        <p className="text-xs text-[#6E6058]">Progestérone (PIO / Ovules) & Œstrogènes</p>
                      </div>
                    </div>
                    <span className="text-xs font-bold bg-[#F6ECE0] text-[#5C3B24] px-3 py-1.5 rounded-full shrink-0">Essentiel</span>
                  </div>
                  <div className="space-y-3">
                    <div className="bg-[#FAF7F3] border border-[#E8DFC9] p-3.5 rounded-xl flex items-center justify-between gap-3">
                      <div className="min-w-0">
                        <p className="text-xs sm:text-sm font-bold text-[#2B2320]">Progestérone en Huile (PIO) 50 mg</p>
                        <p className="text-xs text-[#8D6648]">⏰ 08 h 00 · Injection intramusculaire</p>
                      </div>
                      <input type="checkbox" defaultChecked className="w-6 h-6 accent-[#2B7853] rounded cursor-pointer shrink-0" />
                    </div>
                    <div className="bg-[#FAF7F3] border border-[#E8DFC9] p-3.5 rounded-xl flex items-center justify-between gap-3">
                      <div className="min-w-0">
                        <p className="text-xs sm:text-sm font-bold text-[#2B2320]">Ovules de Progestérone 200 mg</p>
                        <p className="text-xs text-[#8D6648]">⏰ 08 h · 14 h · 22 h (3x / jour)</p>
                      </div>
                      <input type="checkbox" className="w-6 h-6 accent-[#2B7853] rounded cursor-pointer shrink-0" />
                    </div>
                  </div>
                </div>
              </div>

              {/* Vos prochaines actions */}
              <div className="bg-white border border-[#E2D3C4] rounded-2xl p-5 sm:p-6 shadow-sm space-y-4">
                <div className="flex justify-between items-center">
                  <h3 className="font-serif font-bold text-base sm:text-lg text-[#5C3B24]">✓ Vos prochaines actions</h3>
                  <span className="text-[10px] font-extrabold bg-[#EBDCCF] text-[#543722] px-3 py-1.5 rounded-lg">OCTOBRE 2026</span>
                </div>
                <div className="space-y-2.5">
                  {checklist.slice(0, 3).map(item => (
                    <div key={item.id} onClick={() => toggleCheck(item.id)} className="flex items-center gap-3 cursor-pointer p-3 min-h-[48px] hover:bg-[#FAF6F0] rounded-xl transition border border-transparent hover:border-[#E2D3C4]">
                      <input type="checkbox" checked={item.isCompleted} onChange={() => {}} className="w-5 h-5 accent-[#2B7853] cursor-pointer shrink-0" />
                      <div className="flex-1 min-w-0">
                        <p className={`text-sm font-semibold truncate ${item.isCompleted ? 'line-through text-[#6E6058]' : 'text-[#2B2320]'}`}>{item.title}</p>
                        <p className="text-xs text-[#6E6058]">{item.dateText}</p>
                      </div>
                    </div>
                  ))}
                </div>
                <button onClick={() => setCurrentTab('checklist')} className="w-full py-3.5 min-h-[48px] text-xs sm:text-sm font-bold text-[#5C3B24] border border-[#5C3B24] rounded-xl hover:bg-[#F4EBD8] transition flex items-center justify-center">
                  Voir toute la liste de contrôle
                </button>
              </div>
            </div>
          )}

          {/* TAB 2: CALENDAR */}
          {currentTab === 'calendar' && (
            <div className="space-y-6">
              <h2 className="text-2xl font-serif font-bold text-[#5C3B24]">Calendrier Médical du Protocole</h2>
              <div className="bg-white border border-[#E2D3C4] rounded-2xl p-5 sm:p-6 shadow-sm space-y-4">
                <div className="flex justify-between items-center">
                  <span className="font-serif font-bold text-base text-[#5C3B24]">Octobre 2026 (FET & Suivi)</span>
                  <span className="text-xs bg-[#EAF4ED] text-[#2B7853] px-3 py-1.5 rounded-full font-bold">10 Étapes clés</span>
                </div>
                <div className="grid grid-cols-7 gap-2 text-center text-xs font-bold text-[#6E6058]">
                  <span>Lun</span><span>Mar</span><span>Mer</span><span>Jeu</span><span>Ven</span><span>Sam</span><span>Dim</span>
                </div>
                <div className="grid grid-cols-7 gap-2 text-center text-sm">
                  {Array.from({ length: 31 }).map((_, i) => {
                    const day = i + 1;
                    const hasEvent = [1, 2, 6, 9, 11, 13, 26, 28, 30, 31].includes(day);
                    return (
                      <div key={day} className={`p-3.5 min-h-[44px] flex items-center justify-center rounded-xl ${day === 28 ? 'bg-[#5C3B24] text-white font-bold shadow' : hasEvent ? 'bg-[#F7EEE4] font-bold text-[#5C3B24]' : 'text-[#2B2320]'}`}>
                        {day}
                      </div>
                    );
                  })}
                </div>
                <div className="bg-[#FCF9F5] border border-[#E2D3C4] p-4 sm:p-5 rounded-xl space-y-2">
                  <span className="text-xs font-bold text-[#5C3B24] bg-[#F4EBD8] px-3 py-1 rounded inline-block">28 Octobre · 08 h 00</span>
                  <h4 className="font-serif font-bold text-base">Hystérosonographie SIS</h4>
                  <p className="text-sm text-[#6E6058]">RMA East Side (635 Madison Ave). Examen détaillé de la cavité utérine.</p>
                </div>
              </div>
            </div>
          )}

          {/* TAB 3: LAB */}
          {currentTab === 'lab' && (
            <div className="space-y-6">
              <h2 className="text-2xl font-serif font-bold text-[#5C3B24]">Dossier de Laboratoire (RMA NY)</h2>
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                <div className="bg-white border border-[#E2D3C4] p-5 rounded-2xl shadow-sm text-center space-y-2">
                  <span className="text-3xl">🥚</span>
                  <p className="text-3xl font-serif font-bold text-[#5C3B24]">42</p>
                  <p className="text-xs font-semibold text-[#6E6058]">Ovocytes prélevés</p>
                </div>
                <div className="bg-white border border-[#E2D3C4] p-5 rounded-2xl shadow-sm text-center space-y-2">
                  <span className="text-3xl">◉</span>
                  <p className="text-3xl font-serif font-bold text-[#5C3B24]">34</p>
                  <p className="text-xs font-semibold text-[#6E6058]">Ovocytes matures</p>
                </div>
                <div className="bg-white border border-[#E2D3C4] p-5 rounded-2xl shadow-sm text-center space-y-2">
                  <span className="text-3xl">❄</span>
                  <p className="text-3xl font-serif font-bold text-[#5C3B24]">8</p>
                  <p className="text-xs font-semibold text-[#6E6058]">Blastocystes biopsiés</p>
                </div>
                <div className="bg-white border border-[#E2D3C4] p-5 rounded-2xl shadow-sm text-center space-y-2">
                  <span className="text-3xl">🧬</span>
                  <p className="text-3xl font-serif font-bold text-[#5C3B24]">5</p>
                  <p className="text-xs font-semibold text-[#6E6058]">Embryons normaux (euploïdes) · PGT-A ✓</p>
                </div>
              </div>
            </div>
          )}

          {/* TAB 4: CHECKLIST */}
          {currentTab === 'checklist' && (
            <div className="space-y-4">
              <div>
                <span className="text-[10px] font-extrabold text-[#6E6058] tracking-wider uppercase">Priorités du moment</span>
                <h2 className="text-lg sm:text-xl font-serif font-bold text-[#5C3B24]">Liste de contrôle d’exécution immédiate</h2>
                <p className="text-xs sm:text-sm text-[#6E6058]">Cochez les tâches au fur et à mesure pour synchroniser votre avancement.</p>
              </div>
              <div className="space-y-3">
                {checklist.map(item => (
                  <div key={item.id} onClick={() => toggleCheck(item.id)} className="bg-white border border-[#E2D3C4] p-4 rounded-xl shadow-sm flex items-start gap-3 cursor-pointer hover:bg-[#FAF6F0] transition min-h-[48px]">
                    <input type="checkbox" checked={item.isCompleted} onChange={() => {}} className="w-5 h-5 accent-[#2B7853] mt-1 cursor-pointer shrink-0" />
                    <span className="text-xl shrink-0">{item.symbol}</span>
                    <div className="flex-1 min-w-0">
                      <div className="flex justify-between items-center mb-1 gap-2 flex-wrap">
                        <span className="text-[10px] font-bold text-[#5C3B24] uppercase">{item.dateText}</span>
                        <span className="text-[9px] font-bold px-2.5 py-1 rounded-full bg-[#EAF4ED] text-[#2B7853] shrink-0">{item.badgeText}</span>
                      </div>
                      <h4 className={`text-sm sm:text-base font-semibold ${item.isCompleted ? 'line-through text-[#6E6058]' : 'text-[#2B2320]'}`}>{item.title}</h4>
                      <p className="text-xs text-[#6E6058] mt-1 leading-relaxed">{item.description}</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* TAB 5: JOURNAL */}
          {currentTab === 'journal' && (
            <div className="space-y-6">
              <h2 className="text-2xl font-serif font-bold text-[#5C3B24]">Journal de Bord & Notes Personnelles</h2>
              <form onSubmit={addNote} className="bg-white border border-[#E2D3C4] p-5 rounded-2xl shadow-sm space-y-4">
                <input 
                  type="text" 
                  placeholder="Titre de la note..." 
                  value={newNoteTitle} 
                  onChange={e => setNewNoteTitle(e.target.value)} 
                  className="w-full text-sm sm:text-base p-3.5 min-h-[48px] border border-[#E2D3C4] rounded-xl focus:outline-none focus:border-[#5C3B24]"
                />
                <textarea 
                  placeholder="Écrivez vos pensées et observations ici..." 
                  value={newNoteContent} 
                  onChange={e => setNewNoteContent(e.target.value)} 
                  className="w-full text-sm sm:text-base p-3.5 border border-[#E2D3C4] rounded-xl focus:outline-none focus:border-[#5C3B24] h-32"
                />
                <button type="submit" className="w-full py-3.5 min-h-[48px] bg-[#5C3B24] text-white text-sm sm:text-base font-bold rounded-xl hover:bg-[#452B18] transition shadow flex items-center justify-center">
                  Ajouter au Journal
                </button>
              </form>
              <div className="space-y-4">
                {notes.map(note => (
                  <div key={note.id} className="bg-white border border-[#E2D3C4] p-5 rounded-2xl shadow-sm space-y-2.5 relative">
                    <button onClick={() => deleteNote(note.id)} className="absolute top-4 right-4 text-red-500 hover:text-red-700 p-2 min-h-[40px] min-w-[40px] flex items-center justify-center">
                      <Trash2 size={18} />
                    </button>
                    <span className="text-xs font-bold text-[#5C3B24] bg-[#F4EBD8] px-3 py-1 rounded-md inline-block">{note.date}</span>
                    <h4 className="text-base sm:text-lg font-serif font-bold text-[#2B2320] pr-8">{note.title}</h4>
                    <p className="text-sm text-[#6E6058] leading-relaxed">{note.content}</p>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* TAB 6: GROSSESSE / OBSTETRIC JOURNEY */}
          {currentTab === 'obstetric' && (
            <div className="space-y-6">
              <h2 className="text-2xl font-serif font-bold text-[#5C3B24]">Parcours Grossesse (RMA ➔ Naissance)</h2>
              <div className="bg-[#FBF6EE] border border-[#5C3B24] p-6 rounded-2xl text-sm sm:text-base text-[#6E6058] space-y-3 shadow-sm">
                <p className="font-bold text-base sm:text-lg text-[#5C3B24]">Suivi obstétrical détaillé de la graduation à juin 2027 :</p>
                <p>• <strong>Semaines 8–10 (Déc. 2026) :</strong> Graduation RMA & transition vers l'obstétricien (OBGYN).</p>
                <p>• <strong>Semaines 11–13 (Janv. 2027) :</strong> Échographie NT Scan (Clarté nucale) & dépistage prénatal.</p>
                <p>• <strong>Semaines 20–22 (Fév. – Mars 2027) :</strong> Grande Échographie Morphologique (Anatomy Scan).</p>
                <p>• <strong>Semaines 34–36 (Mai 2027) :</strong> Dépistage GBS & préparation au plan de naissance.</p>
                <p>• <strong>Juin 2027 :</strong> Naissance de bébé ! 🎉</p>
              </div>
            </div>
          )}

        </main>

        {/* Mobile Bottom Navigation Bar */}
        <div className="md:hidden fixed bottom-0 left-0 right-0 bg-[#FAF6F0] border-t border-[#E2D3C4] px-1 py-1.5 flex justify-around items-center z-30 shadow-lg">
          {navItems.map(tab => (
            <button
              key={tab.id}
              onClick={() => setCurrentTab(tab.id)}
              className={`flex flex-col items-center justify-center py-2 px-3 min-h-[48px] min-w-[48px] rounded-xl transition ${currentTab === tab.id ? 'text-[#5C3B24] font-bold bg-[#F4EBD8]' : 'text-[#6E6058] hover:text-[#2B2320]'}`}
            >
              <span className="text-xl">{tab.icon}</span>
              <span className="text-[10px] mt-0.5">{tab.label}</span>
            </button>
          ))}
        </div>

      </div>
    </div>
  );
}
