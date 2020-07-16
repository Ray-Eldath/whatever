    mov ax, 0x7c0
    mov ds, ax
    mov ax, gdt_seg ; GDT
    mov es, ax
    mov dword [es:0x00], 0x0000_0000  ; NULL
    mov dword [es:0x04], 0x0000_0000
    mov dword [es:0x08], 0x7c00_01ff  ; code
    mov dword [es:0x0c], 0x0040_9800
    mov word  [es:0x10], data_end - data_start - 1  ; data
    mov word  [es:0x12], 0x7c00 + data_start
    mov dword [es:0x14], 0x0040_9200
    mov dword [es:0x18], 0x8000_ffff  ; graphic card
    mov dword [es:0x1c], 0x0040_920b
    mov dword [es:0x20], 0x7c00_fffe  ; stack
    mov dword [es:0x24], 0x00cf_9600
    lgdt [gdtr]

    cli

    mov eax, cr0
    or eax, 1
    mov cr0, eax
    jmp dword 0x0008:flush

    [bits 32]  ; protect mode ON!!
    flush:
    mov ax, 0x0010
    mov ds, ax
    mov ax, 0x0018
    mov es, ax
    mov ax, 0x0020
    mov ss, ax
    xor esp, esp

    xor di, di
    mov si, notice - data_start
    call print_string

    mov di, 2 * 80  ; 2nd line
    mov si, prompt - data_start
    call print_string
    mov si, chars - data_start
    call print_string

    mov eax, chars - data_start
    .external:
        mov ebx, eax
        .internal:
        mov dh, [eax]
        mov dl, [ebx]
        cmp dh, dl
        jg .internal_xchg
        jmp .internal_end
        .internal_xchg:
        xchg dh, dl
        mov [eax], dh
        mov [ebx], dl
        .internal_end:
        inc ebx
        cmp ebx, data_end - data_start - 1
        jl .internal
    inc eax
    cmp eax, data_end - data_start - 1
    jl .external

    mov di, 4 * 80  ; 3rd line
    mov si, result - data_start
    call print_string
    mov si, chars - data_start
    call print_string

    hlt

    print_string:

    .print_char:  ; input: si
    mov al, [si]
    inc si
    or al, al
    jz .print_char_end
    mov [es:di], al
    inc di
    mov byte [es:di], 0x07
    inc di
    jmp .print_char

    .print_char_end:
    ret

    gdt_seg equ 0x7e0

    data_start:
    gdtr dw 39
         dd gdt_seg * 16
    notice db 'IA-32 Protected Mode ON!'
           db 0
    prompt db 'Before sort: '
           db 0
    result db 'Sorted: '
           db 0
    chars db 'Ray-Eldath'
          db 0
    data_end:

    times 510-($-$$) db 0
                         db 0x55, 0xaa