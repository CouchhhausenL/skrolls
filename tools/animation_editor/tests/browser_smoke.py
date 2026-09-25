"""Optional browser integration test: python tests/browser_smoke.py (requires Playwright)."""
from pathlib import Path
import os
import shutil
import sys
from playwright.sync_api import sync_playwright

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / 'tests'
PROFILE = OUT / '.browser-profile'
CHROME = os.environ.get('SKA_TEST_BROWSER', r'C:\Program Files\Google\Chrome\Application\chrome.exe')


def main():
    shutil.rmtree(PROFILE, ignore_errors=True)
    errors = []
    checks = []
    with sync_playwright() as playwright:
        context = playwright.chromium.launch_persistent_context(
            str(PROFILE), executable_path=CHROME, headless=True,
            viewport={'width': 1440, 'height': 980}, accept_downloads=True,
            downloads_path=str(OUT / '.downloads'),
        )
        page = context.pages[0]
        page.on('pageerror', lambda error: errors.append(str(error)))
        page.on('dialog', lambda dialog: dialog.accept())
        try:
            page.goto((ROOT / 'index.html').as_uri())
            page.wait_for_function("document.querySelector('#art-canvas').width > 0")
            page.wait_for_timeout(200)
            assert page.locator('#frame-label').inner_text() == 'Frame 1 / 1'

            def cell(x, y, button='left', hold=False):
                box = page.locator('#art-canvas').bounding_box()
                width = int(page.locator('#canvas-width').input_value())
                height = int(page.locator('#canvas-height').input_value())
                px = box['x'] + (x + 0.5) * box['width'] / width
                py = box['y'] + (y + 0.5) * box['height'] / height
                page.mouse.move(px, py)
                if hold:
                    page.mouse.down(button=button)
                else:
                    page.mouse.click(px, py, button=button)

            def export():
                with page.expect_download() as event:
                    page.locator('#save-file').click()
                path = OUT / 'browser-export.ska'
                event.value.save_as(path)
                return page.evaluate('(data) => SKA.decode(new Uint8Array(data))', list(path.read_bytes()))

            page.locator('#character-input').fill('X')
            page.locator('#hex-color').fill('#ff0044')
            page.locator('#hex-color').press('Tab')
            cell(3, 3, hold=True)
            assert page.locator('#active-tool').inner_text() == 'Pencil'
            box = page.locator('#art-canvas').bounding_box()
            page.mouse.move(box['x'] + 7.5 * box['width'] / 40, box['y'] + 3.5 * box['height'] / 20)
            page.mouse.up()
            assert page.locator('#active-tool').inner_text() == 'Idle'
            cell(4, 3, 'right')
            document = export()
            assert all(document['frames'][0][3 * 40 + x]['char'] == 'X' for x in [3, 5, 6, 7])
            assert document['frames'][0][3 * 40 + 4] == {'char': ' ', 'fg': '#ffffff', 'bg': '#171a21'}
            checks.append('Pencil drag interpolation, active-tool indicator, right-button eraser, SKA download')

            page.locator('#character-input').fill('Q')
            page.keyboard.down('Control')
            cell(3, 3, 'middle')
            page.keyboard.up('Control')
            assert page.locator('#character-input').input_value() == 'Q'
            assert page.locator('#hex-color').input_value() == '#ff0044'
            assert not page.locator('#bg-none').is_checked()
            cell(3, 3, 'middle')
            assert page.locator('#character-input').input_value() == 'X'
            checks.append('Middle-button picker and Ctrl+middle color-only picker')

            page.locator('#char-none').check()
            page.locator('#bg-none').check()
            page.locator('#hex-color').fill('#4466aa')
            page.locator('#hex-color').press('Tab')
            page.locator('#fill-frame').click()
            document = export()
            assert document['frames'][0][123]['char'] == 'X'
            assert document['frames'][0][0]['char'] == ' '
            assert all(p['fg'] == '#4466aa' for p in document['frames'][0])
            page.locator('#viewport').focus()
            page.keyboard.press('Control+z')
            cell(3, 3, 'middle')
            assert page.locator('#hex-color').input_value() == '#ff0044'
            page.keyboard.press('Control+y')
            cell(3, 3, 'middle')
            assert page.locator('#hex-color').input_value() == '#4466aa'
            checks.append('Partial fill respects None; Ctrl+Z and Ctrl+Y restore cells')

            page.locator('#duplicate-frame').click()
            page.wait_for_timeout(80)
            assert page.locator('#frame-label').inner_text() == 'Frame 2 / 2'
            page.locator('#character-input').fill('Y')
            cell(3, 3)
            page.keyboard.press('a')
            cell(3, 3, 'middle')
            assert page.locator('#character-input').input_value() == 'X'
            page.keyboard.press('ArrowRight')
            cell(3, 3, 'middle')
            assert page.locator('#character-input').input_value() == 'Y'
            page.keyboard.press('Space')
            assert 'Stop' in page.locator('#play').inner_text()
            page.wait_for_timeout(140)
            page.keyboard.press('Space')
            assert 'Play' in page.locator('#play').inner_text()
            grid = page.locator('#grid-toggle').is_checked()
            page.keyboard.press('#')
            assert page.locator('#grid-toggle').is_checked() != grid
            old_zoom = page.locator('#zoom-reset').inner_text()
            page.mouse.wheel(0, -200)
            page.wait_for_timeout(80)
            assert page.locator('#zoom-reset').inner_text() != old_zoom
            checks.append('Independent duplicate frames, A/arrow navigation, playback, grid shortcut, wheel zoom')

            page.locator('#symbols-used').click()
            page.locator('#symbol-search').fill('Y')
            assert page.locator('#symbol-grid button').count() == 1
            page.locator('#symbol-search').fill('')
            before = page.locator('#frame-label').inner_text()
            page.locator('#file-name').fill('ad')
            page.locator('#file-name').press('ArrowLeft')
            assert page.locator('#frame-label').inner_text() == before
            page.locator('#canvas-width').fill('12')
            page.locator('#canvas-height').fill('8')
            page.locator('#resize-canvas').click()
            page.wait_for_function("document.querySelector('#canvas-info').textContent.startsWith('12 × 8')")
            page.locator('#fps').fill('20')
            page.locator('#fps').press('Tab')
            document = export()
            assert (document['width'], document['height'], document['fps']) == (12, 8, 20)
            checks.append('Used-symbol search, input-safe shortcuts, undoable resize, FPS, export metadata')

            page.locator('#select-fg').click()
            page.locator('#hex-color').fill('#102030')
            page.locator('#hex-color').press('Tab')
            count = page.locator('#palette button').count()
            page.locator('#favorite-color').click()
            assert page.locator('#palette button').count() == count + 1
            page.locator('#palette button').last.click(button='right')
            assert page.locator('#palette button').count() == count
            page.locator('#swap-colors').click()
            page.locator('#reset-colors').click()
            assert page.locator('#hex-color').input_value() == '#ffffff'
            wheel = page.locator('#color-wheel').bounding_box()
            page.mouse.click(wheel['x'] + wheel['width'] * 0.85, wheel['y'] + wheel['height'] / 2)
            assert page.locator('#hex-color').input_value() != '#ffffff'
            checks.append('Favorites add/remove, color swap/reset, interactive color wheel')

            bad = OUT / 'invalid.ska'
            bad.write_bytes(b'not an animation')
            page.locator('#file-input').set_input_files(bad)
            page.wait_for_function("document.querySelector('#status').textContent.startsWith('Could not open')")
            assert page.locator('#frame-label').inner_text().endswith('/ 2')
            checks.append('Malformed import rejected without replacing the document')
            bad.unlink()

            for asset in sys.argv[1:]:
                page.locator('#file-input').set_input_files(asset)
                page.wait_for_function("document.querySelector('#status').textContent.startsWith('Opened')")
                document = export()
                assert len(document['frames']) > 1
                checks.append('Real asset import and export: ' + Path(asset).name)

            page.locator('#file-input').set_input_files(ROOT / 'examples' / 'comet.ska')
            page.wait_for_function("document.querySelector('#status').textContent.startsWith('Opened comet')")
            page.locator('#grid-toggle').uncheck()
            page.locator('#symbols-common').click()
            page.locator('#zoom-fit').click()
            page.wait_for_timeout(200)
            page.screenshot(path=str(OUT / 'editor.png'), full_page=True)
            assert not errors, errors
            checks.append('Direct file:// launch; no JavaScript errors; screenshot saved')
            (OUT / 'browser-results.txt').write_text('\n'.join('PASS: ' + check for check in checks) + '\n', encoding='utf-8')
            print('\n'.join('PASS: ' + check for check in checks))
        except Exception:
            page.screenshot(path=str(OUT / 'browser-failure.png'), full_page=True)
            raise
        finally:
            context.close()
    shutil.rmtree(PROFILE, ignore_errors=True)
    shutil.rmtree(OUT / '.downloads', ignore_errors=True)


if __name__ == '__main__':
    main()
