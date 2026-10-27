import React, { useState } from 'react';
import { Heart, Calendar, TestTube, CheckSquare, BookOpen, Baby, Lock, Unlock, Download, Bell, Plus, Trash2, Edit3 } from 'lucide-react';

export default function App() {
  const [isUnlocked, setIsUnlocked] = useState(true);
  const [currentTab, setCurrentTab] = useState('home');
  const [isGoldTheme, setIsGoldTheme] = useState(false);

  // Checklist items state
  const [checklist, setChecklist] = useState([
    { id: 'chk-defrost', symbol: '🧊', title: 'Plan de décongélation terminé', dateText: '13 octobre · soumis', badgeText: '✓ Validé', badgeType: 'past', isCompleted: true, description: 'Formulaire indiquant une préférence pour un garçon (« Male »). Sous réserve des résultats PGT-A.' },
    { id: 'chk-nortrel', symbol: '💊', title: 'Maintien de la médication quotidienne', dateText: 'Chaque jour', badgeText: '⏱️ En cours', badgeType: 'today', isCompleted: false, description: 'Continuer le Nortrel selon les instructions actuelles de l’équipe RMA.' },
    { id: 'chk-sis', symbol: '🏥', title: 'Hystérosonographie SIS', dateText: '28 octobre · 08 h 00', badgeText: '⏳ AUJOURD\'HUI', badgeType: 'today', isCompleted: false, description: 'RMA East Side. Examen pour évaluer la cavité utérine et l\'endomètre.' },
    { id: 'chk-hystero', symbol: '🔬', title: 'Hystéroscopie opératoire', dateText: '30 octobre · 07 h 30', badgeText: '⏳ J-2', badgeType: 'imminent', isCompleted: false, description: 'Gramercy Surgery Center. Intervention pour préparer une cavité utérine optimale avant le FET.' },
    { id: 'chk-pgta', symbol: '🧬', title: 'Surveiller les résultats PGT-A', dateText: '23–28 octobre', badgeText: '⏳ En cours', badgeType: 'today', isCompleted: false, description: 'Analyse chromosomique des 8 blastocystes vitrifiés.' }
  ]);

  // Journal notes state
  const [notes, setNotes] = useState([
    { id: '1', title: 'En attente douce des résultats PGT-A', date: '2026-10-26', category: 'Émotion', content: 'Chaque jour nous rapproche. Rester sereine, respirer profondément, et faire confiance au laboratoire.' },
    { id: '2', title: 'Après la ponction : 42 ovocytes !', date: '2026-10-01', category: 'Jalon', content: '42 ovocytes prélevés, 34 matures et 8 magnifiques blastocystes vitrifiés. Très fière ♡' }
  ]);
  const [newNoteTitle, setNewNoteTitle] = useState('');
  const [newNoteContent, setNewNoteContent] = useState('');

  const toggleCheck = (id) => {
    setChecklist(checklist.map(item => item.id === id ? { ...item, isCompleted: !item.isCompleted } : item));
  };

  const addNote = (e) => {
    e.preventDefault();
    if (!newNoteTitle) return;
    setNotes([{ id: Date.now().toString(), title: newNoteTitle, date: new Date().toISOString().split('T')[0], category: 'Personnel', content: newNoteContent }, ...notes]);
    setNewNoteTitle('');
    setNewNoteContent('');
  };

  const deleteNote = (id) => {
    setNotes(notes.filter(n => n.id !== id));
  };

  const headerBg = isGoldTheme ? 'bg-[#947230]' : 'bg-[#5C3B24]';

  return (
    <div className="min-h-screen bg-[#FAF6F0] text-[#2B2320] flex flex-col items-center justify-start p-2 sm:p-4">
      {/* Mobile Web View Container */}
      <div className="w-full max-w-md bg-[#FFFFFF] shadow-xl rounded-3xl border border-[#E2D3C4] flex flex-col min-h-[92vh] overflow-hidden relative">
        
        {/* Top Header */}
        <div className={`${headerBg} text-white p-4 flex items-center justify-between rounded-t-3xl shadow-md transition-colors duration-300`}>
          <div className="flex items-center gap-2">
            <span className="text-xl text-[#FFDF7D]">♡</span>
            <div>
              <h1 className="text-base font-serif font-bold leading-tight">Mon Parcours</h1>
              <p className="text-[11px] text-[#F0DEC9] font-medium">Fatou ♥ · RMA East Side</p>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <button 
              onClick={() => setIsGoldTheme(!isGoldTheme)}
              className="px-2.5 py-1 text-[10px] font-extrabold bg-black/20 hover:bg-black/30 rounded-full border border-white/20 transition"
            >
              {isGoldTheme ? '✨ Or' : '🤎 Fatou'}
            </button>
            <button 
              onClick={() => alert("Résumé PDF généré et téléchargé avec succès !")}
              className="px-2.5 py-1 text-[10px] font-bold bg-black/30 hover:bg-black/40 rounded-full border border-white/20 transition flex items-center gap-1"
            >
              <Download size={10} /> PDF
            </button>
          </div>
        </div>

        {/* Scrollable Content Area */}
        <div className="flex-1 overflow-y-auto p-4 space-y-4 pb-20">
          
          {/* TAB 1: HOME */}
          {currentTab === 'home' && (
            <div className="space-y-4">
              {/* Hero Banner */}
              <div className="bg-gradient-to-br from-[#F4EBD8] via-[#FCF9F5] to-[#EAE0D0] border border-[#D8C7B3] rounded-2xl p-4 shadow-sm">
                <span className="text-[10px] font-extrabold text-[#5C3B24] tracking-wider">VOTRE PARCOURS FIV</span>
                <h2 className="text-xl font-serif font-medium text-[#2B2320] mt-1">De la première injection au premier battement de cœur</h2>
                <p className="text-xs text-[#6E6058] mt-1">Chaque étape chez RMA of New York est documentée avec précision.</p>
              </div>

              {/* Graduation Countdown Card */}
              <div className="bg-[#FBF6EE] border border-[#5C3B24] rounded-2xl p-4 shadow-sm">
                <div className="flex justify-between items-center mb-2">
                  <div className="flex items-center gap-2">
                    <div className="w-8 h-8 rounded-full bg-[#F4EBD8] flex items-center justify-center text-sm">🎓</div>
                    <div>
                      <h3 className="text-sm font-serif font-bold text-[#5C3B24]">Compte à rebours de Graduation</h3>
                      <p className="text-[10px] text-[#6E6058]">Transition RMA vers OBGYN (Semaines 8–10)</p>
                    </div>
                  </div>
                  <span className="text-[10px] font-bold bg-[#EAF4ED] text-[#2B7853] px-2 py-0.5 rounded-full border border-[#C7E2D0]">Semaine 9</span>
                </div>
                <div className="bg-white rounded-xl p-3 border border-[#E5D4C3] flex justify-between items-center mt-2">
                  <div>
                    <p className="text-[11px] text-[#6E6058]">Âge gestationnel estimé :</p>
                    <p className="text-sm font-serif font-bold text-[#2B2320]">9 semaines et 2 jours</p>
                  </div>
                  <div className="bg-[#FAF6F0] border border-[#E2D3C4] px-3 py-1.5 rounded-lg text-center">
                    <span className="text-lg font-extrabold text-[#5C3B24]">14</span>
                    <p className="text-[8px] font-bold text-[#6E6058]">JOURS CIBLE</p>
                  </div>
                </div>
              </div>

              {/* Continued Support Meds */}
              <div className="bg-white border border-[#E2D3C4] rounded-2xl p-4 shadow-sm">
                <div className="flex justify-between items-center mb-2">
                  <div className="flex items-center gap-2">
                    <div className="w-8 h-8 rounded-full bg-[#EAF4ED] flex items-center justify-center text-sm">💊</div>
                    <div>
                      <h3 className="text-sm font-serif font-bold text-[#2B2320]">Soutien Hormonal Continu</h3>
                      <p className="text-[10px] text-[#6E6058]">Progestérone (PIO / Ovules) & Œstrogènes</p>
                    </div>
                  </div>
                  <span className="text-[10px] font-bold bg-[#F6ECE0] text-[#5C3B24] px-2 py-0.5 rounded-full">Essentiel</span>
                </div>
                <div className="space-y-2 mt-3">
                  <div className="bg-[#FAF7F3] border border-[#E8DFC9] p-2.5 rounded-xl flex items-center justify-between">
                    <div>
                      <p className="text-xs font-bold text-[#2B2320]">Progestérone en Huile (PIO) 50 mg</p>
                      <p className="text-[10px] text-[#8D6648]">⏰ 08 h 00 · Injection intramusculaire</p>
                    </div>
                    <input type="checkbox" defaultChecked className="w-5 h-5 accent-[#2B7853] rounded" />
                  </div>
                  <div className="bg-[#FAF7F3] border border-[#E8DFC9] p-2.5 rounded-xl flex items-center justify-between">
                    <div>
                      <p className="text-xs font-bold text-[#2B2320]">Ovules de Progestérone 200 mg</p>
                      <p className="text-[10px] text-[#8D6648]">⏰ 08 h · 14 h · 22 h (3x / jour)</p>
                    </div>
                    <input type="checkbox" className="w-5 h-5 accent-[#2B7853] rounded" />
                  </div>
                </div>
              </div>

              {/* Vos prochaines actions (Compact) */}
              <div className="bg-white border border-[#E2D3C4] rounded-xl p-3 shadow-sm">
                <div className="flex justify-between items-center mb-2">
                  <h3 className="text-xs font-serif font-bold text-[#5C3B24]">✓ Vos prochaines actions</h3>
                  <span className="text-[8px] font-extrabold bg-[#EBDCCF] text-[#543722] px-2 py-0.5 rounded-md">OCTOBRE 2026</span>
                </div>
                <div className="space-y-2">
                  {checklist.slice(0, 3).map(item => (
                    <div key={item.id} onClick={() => toggleCheck(item.id)} className="flex items-center gap-2 cursor-pointer p-1 hover:bg-[#FAF6F0] rounded-lg">
                      <input type="checkbox" checked={item.isCompleted} onChange={() => {}} className="w-4 h-4 accent-[#2B7853]" />
                      <div className="flex-1 min-w-0">
                        <p className={`text-xs font-semibold truncate ${item.isCompleted ? 'line-through text-[#6E6058]' : 'text-[#2B2320]'}`}>{item.title}</p>
                        <p className="text-[9px] text-[#6E6058]">{item.dateText}</p>
                      </div>
                    </div>
                  ))}
                </div>
                <button onClick={() => setCurrentTab('checklist')} className="w-full mt-2 py-1 text-xs font-bold text-[#5C3B24] border border-[#5C3B24] rounded-lg hover:bg-[#F4EBD8] transition">
                  Voir toute la liste
                </button>
              </div>
            </div>
          )}

          {/* TAB 2: CALENDAR */}
          {currentTab === 'calendar' && (
            <div className="space-y-4">
              <h2 className="text-lg font-serif font-bold text-[#5C3B24]">Calendrier Médical</h2>
              <div className="bg-white border border-[#E2D3C4] rounded-2xl p-4 shadow-sm space-y-3">
                <div className="flex justify-between items-center">
                  <span className="font-serif font-bold text-[#5C3B24]">Octobre 2026 (FET & Suivi)</span>
                  <span className="text-xs bg-[#EAF4ED] text-[#2B7853] px-2 py-1 rounded-full font-bold">10 Étapes</span>
                </div>
                <div className="grid grid-cols-7 gap-1 text-center text-xs font-bold text-[#6E6058]">
                  <span>Lun</span><span>Mar</span><span>Mer</span><span>Jeu</span><span>Ven</span><span>Sam</span><span>Dim</span>
                </div>
                <div className="grid grid-cols-7 gap-1 text-center text-xs">
                  {Array.from({ length: 31 }).map((_, i) => {
                    const day = i + 1;
                    const hasEvent = [1, 2, 6, 9, 11, 13, 26, 28, 30, 31].includes(day);
                    return (
                      <div key={day} className={`p-2 rounded-lg ${day === 28 ? 'bg-[#5C3B24] text-white font-bold' : hasEvent ? 'bg-[#F7EEE4] font-bold text-[#5C3B24]' : 'text-[#2B2320]'}`}>
                        {day}
                      </div>
                    );
                  })}
                </div>
                <div className="bg-[#FCF9F5] border border-[#E2D3C4] p-3 rounded-xl space-y-2">
                  <span className="text-[10px] font-bold text-[#5C3B24] bg-[#F4EBD8] px-2 py-0.5 rounded">28 Octobre · 08 h 00</span>
                  <h4 className="font-serif font-bold text-sm">Hystérosonographie SIS</h4>
                  <p className="text-xs text-[#6E6058]">RMA East Side (635 Madison Ave). Examen de la cavité utérine.</p>
                </div>
              </div>
            </div>
          )}

          {/* TAB 3: LAB */}
          {currentTab === 'lab' && (
            <div className="space-y-4">
              <h2 className="text-lg font-serif font-bold text-[#5C3B24]">Dossier de Laboratoire</h2>
              <div className="grid grid-cols-2 gap-3">
                <div className="bg-white border border-[#E2D3C4] p-3 rounded-xl shadow-sm">
                  <span className="text-xl">🥚</span>
                  <p className="text-2xl font-serif font-bold text-[#5C3B24] mt-1">42</p>
                  <p className="text-xs font-semibold text-[#6E6058]">Ovocytes prélevés</p>
                </div>
                <div className="bg-white border border-[#E2D3C4] p-3 rounded-xl shadow-sm">
                  <span className="text-xl">◉</span>
                  <p className="text-2xl font-serif font-bold text-[#5C3B24] mt-1">34</p>
                  <p className="text-xs font-semibold text-[#6E6058]">Ovocytes matures</p>
                </div>
                <div className="bg-white border border-[#E2D3C4] p-3 rounded-xl shadow-sm">
                  <span className="text-xl">✧</span>
                  <p className="text-2xl font-serif font-bold text-[#5C3B24] mt-1">11</p>
                  <p className="text-xs font-semibold text-[#6E6058]">Fécondés ICSI</p>
                </div>
                <div className="bg-white border border-[#E2D3C4] p-3 rounded-xl shadow-sm">
                  <span className="text-xl">❄</span>
                  <p className="text-2xl font-serif font-bold text-[#5C3B24] mt-1">8</p>
                  <p className="text-xs font-semibold text-[#6E6058]">Blastocystes vitrifiés</p>
                </div>
              </div>
            </div>
          )}

          {/* TAB 4: CHECKLIST */}
          {currentTab === 'checklist' && (
            <div className="space-y-4">
              <h2 className="text-lg font-serif font-bold text-[#5C3B24]">Liste de Contrôle Complète</h2>
              <div className="space-y-2">
                {checklist.map(item => (
                  <div key={item.id} onClick={() => toggleCheck(item.id)} className="bg-white border border-[#E2D3C4] p-3 rounded-xl shadow-sm flex items-start gap-3 cursor-pointer hover:bg-[#FAF6F0]">
                    <input type="checkbox" checked={item.isCompleted} onChange={() => {}} className="w-5 h-5 accent-[#2B7853] mt-0.5" />
                    <div className="flex-1">
                      <div className="flex justify-between items-center mb-1">
                        <span className="text-xs font-bold text-[#5C3B24]">{item.dateText}</span>
                        <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-[#EAF4ED] text-[#2B7853]">{item.badgeText}</span>
                      </div>
                      <h4 className={`text-sm font-semibold ${item.isCompleted ? 'line-through text-[#6E6058]' : 'text-[#2B2320]'}`}>{item.title}</h4>
                      <p className="text-xs text-[#6E6058] mt-1">{item.description}</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* TAB 5: JOURNAL */}
          {currentTab === 'journal' && (
            <div className="space-y-4">
              <h2 className="text-lg font-serif font-bold text-[#5C3B24]">Journal de Bord & Notes</h2>
              <form onSubmit={addNote} className="bg-white border border-[#E2D3C4] p-3 rounded-xl shadow-sm space-y-2">
                <input 
                  type="text" 
                  placeholder="Titre de la note..." 
                  value={newNoteTitle} 
                  onChange={e => setNewNoteTitle(e.target.value)} 
                  className="w-full text-xs p-2 border border-[#E2D3C4] rounded-lg focus:outline-none focus:border-[#5C3B24]"
                />
                <textarea 
                  placeholder="Écrivez vos pensées ici..." 
                  value={newNoteContent} 
                  onChange={e => setNewNoteContent(e.target.value)} 
                  className="w-full text-xs p-2 border border-[#E2D3C4] rounded-lg focus:outline-none focus:border-[#5C3B24] h-20"
                />
                <button type="submit" className="w-full py-1.5 bg-[#5C3B24] text-white text-xs font-bold rounded-lg hover:bg-[#452B18] transition">
                  Ajouter au Journal
                </button>
              </form>
              <div className="space-y-3">
                {notes.map(note => (
                  <div key={note.id} className="bg-white border border-[#E2D3C4] p-3 rounded-xl shadow-sm space-y-1 relative">
                    <button onClick={() => deleteNote(note.id)} className="absolute top-3 right-3 text-red-500 hover:text-red-700">
                      <Trash2 size={14} />
                    </button>
                    <span className="text-[10px] font-bold text-[#5C3B24] bg-[#F4EBD8] px-2 py-0.5 rounded">{note.date}</span>
                    <h4 className="text-sm font-serif font-bold text-[#2B2320]">{note.title}</h4>
                    <p className="text-xs text-[#6E6058]">{note.content}</p>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* TAB 6: GROSSESSE / OBSTETRIC JOURNEY */}
          {currentTab === 'obstetric' && (
            <div className="space-y-4">
              <h2 className="text-lg font-serif font-bold text-[#5C3B24]">Parcours Grossesse (RMA ➔ Naissance)</h2>
              <div className="bg-[#FBF6EE] border border-[#5C3B24] p-3 rounded-xl text-xs text-[#6E6058] space-y-1">
                <p className="font-bold text-[#5C3B24]">Suivi obstétrical de la graduation à juin 2027 :</p>
                <p>• <strong>S8-S10 :</strong> Graduation RMA & transition OBGYN.</p>
                <p>• <strong>S12 :</strong> Échographie NT Scan (Clarté nucale).</p>
                <p>• <strong>S20 :</strong> Grande échographie morphologique (Anatomy Scan).</p>
                <p>• <strong>Juin 2027 :</strong> Naissance de bébé !</p>
              </div>
            </div>
          )}

        </div>

        {/* Bottom Navigation Bar */}
        <div className="absolute bottom-0 left-0 right-0 bg-[#FAF6F0] border-t border-[#E2D3C4] px-2 py-2 flex justify-around items-center z-10 shadow-lg">
          {[
            { id: 'home', label: 'Accueil', icon: '⌂' },
            { id: 'calendar', label: 'Agenda', icon: '▦' },
            { id: 'lab', label: 'Labo', icon: '🧪' },
            { id: 'checklist', label: 'Actions', icon: '☑' },
            { id: 'journal', label: 'Notes', icon: '✍' },
            { id: 'obstetric', label: 'Bébé', icon: '👶' },
          ].map(tab => (
            <button
              key={tab.id}
              onClick={() => setCurrentTab(tab.id)}
              className={`flex flex-col items-center py-1 px-2 rounded-xl transition ${currentTab === tab.id ? 'text-[#5C3B24] font-bold bg-[#F4EBD8]' : 'text-[#6E6058] hover:text-[#2B2320]'}`}
            >
              <span className="text-base">{tab.icon}</span>
              <span className="text-[9px] mt-0.5">{tab.label}</span>
            </button>
          ))}
        </div>

      </div>
    </div>
  );
}
