import { Component, ElementRef, ViewChild, AfterViewChecked } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { trigger, state, style, transition, animate } from '@angular/animations';
import { ChatbotService } from '../../../core/services/chatbot.service';

// Interface pour un message dans le chat
interface Message {
  text: string;
  isUser: boolean;
}

@Component({
  selector: 'app-chatbot',
  standalone: false,
  templateUrl: './chatbot.component.html',
  styleUrls: ['./chatbot.component.scss'],
  animations: [
    // Animation pour l'apparition de la fenêtre de chat
    trigger('slideInOut', [
      state('closed', style({
        transform: 'translateY(100%) translateX(100%) scale(0)',
        opacity: 0,
        visibility: 'hidden'
      })),
      state('open', style({
        transform: 'translateY(0) translateX(0) scale(1)',
        opacity: 1,
        visibility: 'visible'
      })),
      transition('closed <=> open', [
        animate('300ms ease-in-out')
      ]),
    ])
  ]
})

export class ChatbotComponent implements AfterViewChecked {
  
  @ViewChild('chatBody') private chatBodyRef!: ElementRef;

  chatState: 'open' | 'closed' = 'closed';
  isLoading = false;
  chatForm: FormGroup;
  messages: Message[] = [
    { text: "Bonjour ! Je suis EduBot, votre tuteur IA. Comment puis-je vous aider avec vos cours aujourd'hui ?", isUser: false }
  ];

  constructor(private fb: FormBuilder, private chatbotService: ChatbotService) {
    this.chatForm = this.fb.group({
      question: ['', Validators.required]
    });
  }

  ngAfterViewChecked() {
    this.scrollToBottom();
  }

  toggleChat(): void {
    this.chatState = this.chatState === 'closed' ? 'open' : 'closed';
  }

  sendMessage(): void {
    if (this.chatForm.invalid) return;

    const userMessageText = this.chatForm.value.question;
    this.messages.push({ text: userMessageText, isUser: true });
    this.isLoading = true;
    this.chatForm.reset();

    this.chatbotService.ask(userMessageText).subscribe({
      next: (response) => {
        // Simuler une petite attente pour un effet plus naturel
        setTimeout(() => {
          this.messages.push({ text: response.answer, isUser: false });
          this.isLoading = false;
        }, 500);
      },
      error: () => {
        setTimeout(() => {
          this.messages.push({ text: "Désolé, une erreur est survenue. Veuillez réessayer.", isUser: false });
          this.isLoading = false;
        }, 500);
      }
    });
  }

  private scrollToBottom(): void {
    try {
      this.chatBodyRef.nativeElement.scrollTop = this.chatBodyRef.nativeElement.scrollHeight;
    } catch(err) { }
  }
}