    gdt_seg equ 0x7e0
    idt equ 0x1f000

SECTION mbr vstart=0x7c00
    mov ax, gdt_seg
    mov es, ax
    mov dword [es:0x08], 0x0000_ffff  ; flat code
    mov dword [es:0x0c], 0x00cf_9800
    mov dword [es:0x10], 0x0000_ffff  ; flat data & stack
    mov dword [es:0x14], 0x00cf_9200
    lgdt [gdtr]

    cli

    mov eax, cr0
    or eax, 1
    mov cr0, eax
    mov eax, 0x0010
    mov ds, eax
    mov eax, 0x0010
    mov ss, eax
    jmp dword 0x0008:flush

    USE32  ; protect mode ON!!
    flush:
    mov dword [0x20000], 0x00021_103
    xor eax, eax
    mov edi, 0x21000
    mov ecx, 256

    allocate_page:
    push eax
    shl eax, 12
    or eax, 0x00000_003
    mov [edi], eax
    add edi, 4
    pop eax
    inc eax
    loop allocate_page

    mov eax, 0x00020_000
    mov cr3, eax
    mov eax, cr0
    or eax, 0x8000_0000
    mov cr0, eax  ; enable page

    mov edi, 0xb8000
    mov esi, prompt
    call print_string

    ; build & install general interrupt handler
    mov eax, general_interrupt_handler
    call .build_interrupt_gate
    mov ecx, 254
    xor ebx, ebx
    .install_general_interrupt_handler:
    mov [idt + ebx], eax
    mov [idt + ebx + 4], edx
    add ebx, 4
    loop .install_general_interrupt_handler

    ; build & install intrrupt handler for #GP(13)
    mov eax, code_exception_handler
    call .build_interrupt_gate
    mov [idt + 13 * 8], eax
    mov [idt + 13 * 8 + 4], edx

    mov eax, rtm_interrupt_handler
    call .build_interrupt_gate
    mov [idt + 0x70 * 8], eax
    mov [idt + 0x70 * 8 + 4], edx

    lidt [idtr]

    ; enable interruption in RTC
    mov al, 0x0b  ; register B
    out 0x70, al
    mov al, 0x12
    out 0x71, al
    mov al, 0x0c  ; touch C
    out 0x70, al
    in al, 0x71

    ; enable RTC interruption in 8259
    in al, 0xa1
    and al, 0xfe
    out 0xa1, al

    sti

    .halt:
    hlt
    not byte [0xb8001]
    jmp .halt

    .build_interrupt_gate:  ; offset at eax -> lower dword in eax, upper dword in edx
    xor edx, edx
    push ebx
    mov ebx, eax

    mov eax, cs
    shl eax, 16
    push ebx
    and ebx, 0x0000_ffff
    or eax, ebx
    pop ebx
    and ebx, 0xffff_0000
    mov edx, 0x0000_8e00
    or edx, ebx
    pop ebx
    ret

; vector < 20 => exception; vector > 32 => interrupt
code_exception_handler:
    pushad

    mov esi, code_exception
    mov edi, 0xb8000
    add word [line_chars_count], 80
    add edi, [line_chars_count]
    call print_string
    mov eax, [esp + 2 * 4]  ; error code
    call print_hex

    popad
    add esp, 4
    iretd

general_interrupt_handler:
    push eax
    mov al, 0x20
    out 0x20, al
    out 0xa0, al
    pop eax
    iretd

rtm_interrupt_handler:
    push eax
    mov byte [edi], '@'
    inc edi
    mov byte [edi], 0x07
    inc edi

    ; touch register C
    mov al, 0x0c
    out 0x70, al
    in al, 0x71

    ; send EOI
    mov al, 0x20
    out 0x20, al  ; master
    out 0xa0, al  ; slave

    pop eax
    iretd

print_hex:  ; content at eax
    push ecx
    push ebx
    mov ebx, hex_to_char
    mov ecx, 8

    .print_hex_digit:
    rol eax, 4
    and eax, 0x0000_000f
    xlat
    mov [edi], al
    inc edi
    mov byte [edi], 0x07
    inc edi
    loop .print_hex_digit

    pop ebx
    pop ecx
    ret

print_string:  ; address at esi
    push eax
    print_char:
    mov al, [esi]
    inc esi
    cmp al, 0
    jz print_string_end
    mov [edi], al
    inc edi
    mov byte [edi], 0x07
    inc edi
    jmp print_char
    print_string_end:
    pop eax
    ret

data_start:
    gdtr dw 31
         dd gdt_seg * 16
    idtr dw 2047
         dd idt
    prompt db 'tick '
           db 0
    code_exception db 'exception errorcode: '
                   db 0
    hex_to_char db '0123456789ABCDEF'
    line_chars_count dw 80
data_end:

    times (510 - ($ - $$)) db 0
                           db 0x55,0xaa