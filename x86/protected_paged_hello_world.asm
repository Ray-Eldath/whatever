    gdt_seg equ 0x7e0

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

    hlt

    print_string:  ; input: address at esi
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
    prompt db 'Hello World!'
           db 0
    data_end:

    times (510 - ($ - $$)) db 0
                           db 0x55,0xaa